package rpc.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public class NettyClientFactory {

    private final Class<? extends Channel> channelType;
    private final Map<ChannelOption<?>, ?> channelOptions;
    private final EventLoopGroup group;

    public NettyClientFactory(Class<? extends Channel> channelType, Map<ChannelOption<?>, ?> channelOptions,EventLoopGroup group){
        this.channelType = channelType;
        this.channelOptions = new HashMap<ChannelOption<?>, Object>(channelOptions);

        if(group == null){
            boolean useDaemonThreads = true;
            ThreadFactory threadFactory = new DefaultThreadFactory("rpc-default-worker-ELG", useDaemonThreads);
            int parallelism =  Runtime.getRuntime().availableProcessors() * 2;
            this.group = new NioEventLoopGroup(parallelism, threadFactory);
        }else{
            this.group = group;
        }
    }
    public NettyClient newClient(SocketAddress serverAddress){
        NettyClient nettyClient = new NettyClient(serverAddress,group,channelType,channelOptions);


        return nettyClient;
    }
}
