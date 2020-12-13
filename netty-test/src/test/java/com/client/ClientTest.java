package com.client;

import com.A;
import com.HelloWord;
import com.P;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.client.NettyBuilder;
import rpc.client.NettyChannel;
import rpc.client.NettyClient;
import rpc.client.NettyClientFactory;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;
import rpc.proxy.DefautRpcProxyFatory;
import rpc.proxy.RpcProxy;
import rpc.proxy.RpcProxyFatory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        final AtomicLong requestCounter = new AtomicLong();


        NettyChannel nettyChannel = NettyBuilder.forHost("127.0.0.1", 12345).build();

        Class<HelloWord> helloWordClass = HelloWord.class;
        RpcProxyFatory rpcProxyFatory = new DefautRpcProxyFatory();
        RpcProxy<HelloWord> proxy = rpcProxyFatory.getProxy(helloWordClass, nettyChannel);
        HelloWord helloWord = proxy.getProxy();
        P p = new P();
        p.setA("你说");
        p.setB("什么呢");
        A sss = helloWord.sss(p);

        System.out.println(sss);

//        for(int i = 0;i<1;i++) {
//            RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
//            request.setVersion(1234);
//            request.setClassName(HelloWord.class.getName());
//            request.setMethodName(HelloWord.class.getMethods()[1].getName());
//            request.setParameterClasses(HelloWord.class.getMethods()[1].getParameterTypes());
//            request.setParameters(new Object[]{"先生","在干什么"});
//            request.setRequestId(String.valueOf(requestCounter.incrementAndGet()));
////            System.out.println(request);
//            CompletableFuture<RpcResponse> completableFuture = nettyClient.send(request);
//            RpcResponse rpcRespons = completableFuture.get();
//            RpcResponse rpcResponse = null;
//                rpcResponse = completableFuture.get();
//            System.out.println(rpcRespons);
//        }

//        Thread.sleep(Integer.MAX_VALUE);
    }
}
