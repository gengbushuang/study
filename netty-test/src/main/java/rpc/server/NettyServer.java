package rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.logging.Logger;

public class NettyServer {

    private static final Logger log = Logger.getLogger(NettyServer.class.getName());

    private volatile boolean shutdown;

    private final SocketAddress address;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private Channel channel;

    public NettyServer(SocketAddress address, EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        this.address = address;
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
    }

    public void start() throws IOException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        synchronized (NettyServer.this) {
                            System.out.println(channel.isOpen() + "--" + channel.localAddress() + "--" + channel.remoteAddress());
                            if (channel != null && !channel.isOpen()) {
                                // Server already shutdown.
                                channel.close();
                                shutdown = true;
                                return;
                            }
                        }
                        channel.pipeline()
                                .addLast(new NettyServerHandle()); // 处理 RPC 请求
                    }
                });

        ChannelFuture future = bootstrap.bind(address);
        try {
            future.await();
        } catch (InterruptedException ex) {
            shutdown = true;
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            shutdown = true;
            throw new IOException("Failed to bind", future.cause());
        }
        channel = future.channel();
        log.info("server started on address " + address);
        shutdown = false;
    }

    public boolean shutdown() {
        if (channel == null || !channel.isOpen()) {
            shutdown = true;
            return shutdown;
        }

        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    log.warning("Error shutting down server" + future.cause());
                }
                synchronized (NettyServer.this) {
                    shutdown = true;
                }
            }
        });
        return shutdown;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public static void main(String[] args) {

    }
}
