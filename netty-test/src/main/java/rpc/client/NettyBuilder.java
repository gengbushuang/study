package rpc.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

public class NettyBuilder {

    private final Map<ChannelOption<?>, Object> channelOptions =
            new HashMap<ChannelOption<?>, Object>();

    private Class<? extends Channel> channelType = NioSocketChannel.class;


    private EventLoopGroup eventLoopGroup;



    private final SocketAddress address;

//    NettyBuilder(String host,int port){
//
//    }
    public NettyBuilder(SocketAddress address){
        this.address = address;
    }

    public static NettyBuilder forHost(String hots, int port) {
       return new NettyBuilder(new InetSocketAddress(hots,port));
    }

    public NettyBuilder channelType(Class<? extends Channel> channelType) {
        this.channelType = channelType;
        return this;
    }

    public <T> NettyBuilder withOption(ChannelOption<T> option, T value){
        this.channelOptions.put(option,value);
        return this;
    }

    public NettyBuilder eventLoopGroup(EventLoopGroup eventLoopGroup){
        this.eventLoopGroup = eventLoopGroup;
        return this;
    }

    public NettyClientFactory buildNettyClientFactory() {
        return new NettyClientFactory(channelType,channelOptions,eventLoopGroup);
    }


    public NettyChannel build() throws InterruptedException {
        return new NettyChannel(this,buildNettyClientFactory(),address);
    }

}
