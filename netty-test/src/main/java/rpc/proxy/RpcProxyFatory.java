package rpc.proxy;

import io.netty.channel.Channel;
import rpc.client.NettyChannel;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;

public interface RpcProxyFatory {

	public  <T>  RpcProxy<T> getProxy(Class<T> protocol, NettyChannel channel, InvocationHandler invocationHandler) throws IOException;

	public  <T>  RpcProxy<T> getProxy(Class<T> protocol, NettyChannel channel) throws IOException;
}
