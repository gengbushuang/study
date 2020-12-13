package rpc.proxy;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import rpc.client.NettyChannel;
import rpc.concurrent.ListenableFuture;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;
import rpc.utils.LogId;

import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class Invoker implements RpcInvocationHandler {

    private final AtomicLong requestCounter = new AtomicLong();

    private final LogId logId;
    private final Class<?> protocol;
    private NettyChannel channel;

    public Invoker(Class<?> _protocol, NettyChannel channel) {
        this.protocol = _protocol;
        this.channel = channel;
        this.logId = LogId.allocate(_protocol.getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setVersion(1234);
        request.setRequestId(logId.toString()+"-"+requestCounter.incrementAndGet());
        request.setClassName(protocol.getName());
        request.setMethodName(method.getName());
        request.setParameterClasses(method.getParameterTypes());
        request.setParameters(args);


        CompletableFuture<RpcResponse> completableFuture = channel.send(request);

        RpcResponse rpcResponse = completableFuture.get();
        return rpcResponse.getResult();
    }
}