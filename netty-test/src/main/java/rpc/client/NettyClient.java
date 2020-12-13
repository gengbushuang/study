package rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NettyClient {

    private final EventLoopGroup group;

    private final SocketAddress address;

    private final Class<? extends Channel> channelType;

    private final Map<ChannelOption<?>, ?> channelOptions;

    private Channel channel;

    private NettyClientHandle handler;

    NettyClient(SocketAddress address, EventLoopGroup group, Class<? extends Channel> channelType, Map<ChannelOption<?>, ?> channelOptions) {
        this.address = address;
        this.group = group;
        this.channelType = channelType;
        this.channelOptions = channelOptions;
    }


    public void start() throws InterruptedException {
        EventLoop eventLoop = group.next();

        handler = NettyClientHandle.newHandle();


        Bootstrap b = new Bootstrap();
        b.group(eventLoop)
                .channel(channelType)
                .handler(handler);

        ChannelFuture f = b.connect(address).sync();
        channel = f.channel();
        handler.startWriteQueue(channel);

        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println(future.isDone() + "-" + future.isSuccess() + "-" + future.isCancelled() + "-" + future.isCancellable());
                // Typically we should have noticed shutdown before this point.
            }
        });

//        b.group(eventLoop);
//        b.channel(channelType);
//
//        b.handler(handler);
//
//        ChannelFuture regFuture = b.register();
//        channel = regFuture.channel();
//        if (channel == null) {
//
//        }
//        System.out.println(regFuture.isDone());
//
//        handler.startWriteQueue(channel);
//
//        channel.connect(address);
//
//        channel.writeAndFlush(new Object()).addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println(future.isDone()+"-"+future.isSuccess()+"-"+future.isCancelled()+"-"+future.isCancellable());
//                if (!future.isSuccess()) {
//
//                }
//            }
//        });
//
//
//        channel.closeFuture().addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println(future.isDone()+"-"+future.isSuccess()+"-"+future.isCancelled()+"-"+future.isCancellable());
//                System.out.println("Connection closed with unknown cause");
//            }
//        });

    }


    Channel channel() {
        return channel;
    }

    public CompletableFuture<RpcResponse> send(RpcRequest request) {
        return handler.getWriteQueue().enqueue(request);
    }
}
