package rpc.test;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.client.NettyBuilder;
import rpc.client.NettyChannel;
import rpc.proxy.DefautRpcProxyFatory;
import rpc.proxy.RpcProxy;
import rpc.proxy.RpcProxyFatory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NettyClientTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        NettyBuilder nettyBuilder = new NettyBuilder(InetSocketAddress.createUnresolved("127.0.0.1",8989));
        NettyChannel channel = nettyBuilder.eventLoopGroup(new NioEventLoopGroup()).channelType(NioSocketChannel.class).build();

        RpcProxyFatory proxyFatory = new DefautRpcProxyFatory();
        RpcProxy<HelloWord> proxy = proxyFatory.getProxy(HelloWord.class, channel);
        HelloWord helloWord = proxy.getProxy();
        String result = helloWord.sysHello("天天");
    }
}
