package com.server;

import com.HelloWorldImpl;
import io.netty.channel.nio.NioEventLoopGroup;
import rpc.server.InterfaceRegistry;
import rpc.server.NettyServer;
import rpc.server.NettyServiceBuilder;

import java.io.IOException;

public class ServerTest {

    public static void main(String[] args) throws IOException {

        InterfaceRegistry registry = InterfaceRegistry.getInstance();
        registry.registry(new HelloWorldImpl());


        NettyServiceBuilder serviceBuilder = NettyServiceBuilder.forPort(12345)
                .bossEventLoopGroup(new NioEventLoopGroup(1))
                .workerEventLoopGroup(new NioEventLoopGroup(4));

        NettyServer nettyServer = serviceBuilder.build();
        nettyServer.start();
    }
}
