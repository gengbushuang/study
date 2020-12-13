package rpc.proxy;


import io.netty.channel.Channel;
import rpc.client.NettyChannel;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DefautRpcProxyFatory implements RpcProxyFatory {

    @Override
    public <T> RpcProxy<T> getProxy(Class<T> protocol, NettyChannel channel, InvocationHandler invocationHandler) throws IOException {
        T t = (T) Proxy.newProxyInstance(protocol.getClassLoader(), new Class[]{protocol}, invocationHandler);
        return new RpcProxy<T>(protocol, t);
    }

    @Override
    public <T> RpcProxy<T> getProxy(Class<T> protocol, NettyChannel channel) throws IOException {
        Invoker invoker = new Invoker(protocol, channel);
        return this.getProxy(protocol, channel, invoker);
    }

}
