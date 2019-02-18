package rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutorService;

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
}
