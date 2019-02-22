package rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.AsyncUtility;
import raft.RaftRequestMessage;
import raft.RaftResponseMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

public class RpcClient {

    private Logger logger;

    // 地址
    private InetSocketAddress remote;

    private AsynchronousSocketChannel connection;
    private AsynchronousChannelGroup channelGroup;

    public RpcClient(InetSocketAddress remote, ExecutorService executorService) {

        this.remote = remote;
        this.logger = LogManager.getLogger(getClass());
        try {
            this.channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
        } catch (IOException err) {

        }
    }

    public synchronized CompletableFuture<RaftResponseMessage> send(final RaftRequestMessage request) {
        CompletableFuture<RaftResponseMessage> result = new CompletableFuture<>();
        if (this.connection == null || !this.connection.isOpen()) {
            try {
                this.connection = AsynchronousSocketChannel.open(this.channelGroup);
                this.connection.connect(this.remote, new AsyncTask<RaftRequestMessage>(request, result), handlerFrom((Void v, AsyncTask<RaftRequestMessage> task) -> {
                    sendAndRead(task, false);
                }));
            } catch (Throwable e) {

            }
        }else{

        }

        return result;
    }

    private void sendAndRead(AsyncTask<RaftRequestMessage> task, boolean skipQueueing) {
        task.input

    }

    private <V, I> CompletionHandler<V, AsyncTask<I>> handlerFrom(BiConsumer<V, AsyncTask<I>> consumer) {
        return AsyncUtility.handlerFrom(consumer, (Throwable exc, AsyncTask<I> context) -> {
            this.logger.info("socket error", exc);
            context.future.completeExceptionally(exc);
            closeSocket();
        });
    }

    private synchronized void closeSocket() {
        this.logger.debug("close the socket due to errors");
        try {
            if (this.connection != null) {
                this.connection.close();
                this.connection = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class AsyncTask<TInput> {
        private TInput input;
        private CompletableFuture<RaftResponseMessage> future;

        public AsyncTask(TInput input, CompletableFuture<RaftResponseMessage> future) {
            this.input = input;
            this.future = future;
        }
    }
}
