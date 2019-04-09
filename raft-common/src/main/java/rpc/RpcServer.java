package rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.*;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.util.function.BiConsumer;

/**
 * Created by gbs on 19/3/27.
 */
public class RpcServer {
    private int port;

    private Logger logger;

    private AsynchronousServerSocketChannel listener;

    private ExecutorService executorService;

    private List<AsynchronousSocketChannel> connections;

    public RpcServer(int port, ExecutorService executorService) {
        this.port = port;
        this.executorService = executorService;
        this.logger = LogManager.getLogger(getClass());
        this.connections = Collections.synchronizedList(new LinkedList<AsynchronousSocketChannel>());
    }

    public void listener(RaftMessageHandler messageHandler) {
        try {
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(this.executorService);
            this.listener = AsynchronousServerSocketChannel.open(channelGroup);
            this.listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            this.listener.bind(new InetSocketAddress(this.port));
            this.acceptRequests(messageHandler);
        } catch (IOException e) {
            logger.error("failed to start the listener due to io error", e);
        }
    }

    private void acceptRequests(RaftMessageHandler messageHandler) {

        this.listener.accept(messageHandler, AsyncUtility.handlerFrom(
                (AsynchronousSocketChannel connection, RaftMessageHandler handler) -> {
                    this.connections.add(connection);
                    acceptRequests(handler);
                    readRequest(connection, handler);
                },
                (Throwable error, RaftMessageHandler handler) -> {
                    logger.error("accepting a new connection failed, will still keep accepting more requests", error);
                    acceptRequests(handler);
                }));
    }

    private void readRequest(AsynchronousSocketChannel connection, RaftMessageHandler messageHandler) {
        ByteBuffer buffer = ByteBuffer.allocate(BinaryUtils.RAFT_REQUEST_HEADER_SIZE);
        try {
            AsyncUtility.readFromChannel(connection, buffer, messageHandler, handlerFrom((Integer byteRead, final RaftMessageHandler handler) -> {
                if (byteRead.intValue() < BinaryUtils.RAFT_REQUEST_HEADER_SIZE) {
                    logger.info("failed to read the request header from client socket");
                    closeSocket(connection);
                } else {
                    try {
                        final Pair<RaftRequestMessage, Integer> requestInfo = BinaryUtils.bytesToRequestMessage(buffer.array());
                        if (requestInfo.getSecond().intValue() > 0) {
                            ByteBuffer logBuffer = ByteBuffer.allocate(requestInfo.getSecond().intValue());
                            AsyncUtility.readFromChannel(connection, logBuffer, null, handlerFrom((Integer size, Object attachment) -> {
                                if (size.intValue() < requestInfo.getSecond().intValue()) {
                                    logger.info("failed to read the log entries data from client socket");
                                    closeSocket(connection);
                                } else {
                                    try {
                                        requestInfo.getFirst().setLogEntries(BinaryUtils.bytesToLogEntries(logBuffer.array()));
                                        processRequest(connection, requestInfo.getFirst(), handler);
                                    } catch (Throwable e) {
                                        logger.info("log entries parsing error", e);
                                        closeSocket(connection);
                                    }
                                }
                            }, connection));
                        } else {
                            processRequest(connection, requestInfo.getFirst(), handler);
                        }
                    } catch (Throwable e) {
                        closeSocket(connection);
                        logger.info("message reading/parsing error", e);
                    }
                }
            }, connection));
        } catch (Exception e) {
            logger.info("failed to read more request from client socket", e);
            closeSocket(connection);
        }
    }

    private void processRequest(AsynchronousSocketChannel connection, RaftRequestMessage request, RaftMessageHandler messageHandler) {
        try {
            RaftResponseMessage response = messageHandler.processRequest(request);
            final ByteBuffer buffer = ByteBuffer.wrap(BinaryUtils.messageToBytes(response));
            AsyncUtility.writeToChannel(connection, buffer, null, handlerFrom((Integer bytesSet, Object attachment) -> {
                if (bytesSet.intValue() < buffer.limit()) {
                    closeSocket(connection);
                } else {
                    logger.debug("response message sent.");
                    if (connection.isOpen()) {
                        logger.debug("try to read next request");
                        readRequest(connection, messageHandler);
                    }
                }
            }, connection));
        } catch (Throwable e) {
            closeSocket(connection);
            logger.error("failed to process the request or send the response", e);
        }
    }

    private <V, A> CompletionHandler<V, A> handlerFrom(BiConsumer<V, A> completed, AsynchronousSocketChannel connection) {
        return AsyncUtility.handlerFrom(completed, (Throwable error, A attachment) -> {
            if (connection != null) {
                closeSocket(connection);
            }
        });
    }

    private void closeSocket(AsynchronousSocketChannel connection) {
        try {
            this.connections.remove(connection);
            connection.close();
        } catch (IOException ex) {
            this.logger.info("failed to close client socket", ex);
        }
    }
}
