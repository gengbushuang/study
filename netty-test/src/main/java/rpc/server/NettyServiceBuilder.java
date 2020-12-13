package rpc.server;

import io.netty.channel.EventLoopGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;

public class NettyServiceBuilder {

    private final SocketAddress address;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;


    private NettyServiceBuilder(SocketAddress address) {
        this.address = address;
    }

    public static NettyServiceBuilder forPort(int port) {
        return new NettyServiceBuilder(new InetSocketAddress(port));
    }

    public static NettyServiceBuilder forAddress(SocketAddress address) {
        return new NettyServiceBuilder(address);
    }


    public NettyServiceBuilder bossEventLoopGroup(EventLoopGroup group) {
        this.bossGroup = group;
        return this;
    }

    public NettyServiceBuilder workerEventLoopGroup(EventLoopGroup group) {
        this.workerGroup = group;
        return this;
    }

    public NettyServer build() {
        return new NettyServer(address,
                bossGroup,
                workerGroup);
    }
}
