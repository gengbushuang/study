package rpc.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPromise;
import rpc.model.RpcRequest;
import rpc.model.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.LockSupport;

public class WriteQueue {

    static final int DEQUE_CHUNK_SIZE = 128;

    private final Channel channel;

    private final Map<String, CompletableFuture<RpcResponse>> skipListMap = new ConcurrentSkipListMap<>();

    public WriteQueue(Channel channel) {
        this.channel = channel;
    }

    public CompletableFuture<RpcResponse> enqueue(RpcRequest request) {
        return this.enqueue(request, channel.newPromise());
    }

    CompletableFuture<RpcResponse> enqueue(RpcRequest request, ChannelPromise promise) {
        final CompletableFuture<RpcResponse> completableFuture = new CompletableFuture();

        promise.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                    completableFuture.completeExceptionally(cause);
                    return;
                }
                if (future.isSuccess()) {
//                    System.out.println("put--"+request.getRequestId());
                    skipListMap.put(request.getRequestId(), completableFuture);
                    while (skipListMap.size() > DEQUE_CHUNK_SIZE) {
                        System.out.println("sleep");
                        LockSupport.parkUntil(100);
                    }
                }
            }
        });
        channel.writeAndFlush(request, promise);
        return completableFuture;
    }

    private void write(ByteBuf out, ChannelPromise promise) {
        channel.write(out, promise);
    }

    public void remove(RpcResponse response) {
//        System.out.println("remove--"+response.getRequestId());
        CompletableFuture<RpcResponse> completableFuture = skipListMap.remove(response.getRequestId());
        if (completableFuture != null) {
            completableFuture.complete(response);
        }
    }

}
