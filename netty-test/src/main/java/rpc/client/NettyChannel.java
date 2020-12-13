package rpc.client;

import rpc.model.RpcRequest;
import rpc.model.RpcResponse;

import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;

public class NettyChannel {

    NettyClientFactory nettyFactory;

    NettyClient nettyClient;


    public NettyChannel(NettyBuilder nettyBuilder, NettyClientFactory nettyFactory, SocketAddress address) throws InterruptedException {
        this.nettyFactory = nettyFactory;
        this.nettyClient = nettyFactory.newClient(address);
        this.nettyClient.start();
    }

    public CompletableFuture<RpcResponse> send(RpcRequest request) {

        CompletableFuture<RpcResponse> future = nettyClient.send(request);

        return future;
    }

}
