package labrpc.aio;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
import java.util.function.BiConsumer;

public class AioServer {
    private int port;
    private Logger logger;
    private AsynchronousServerSocketChannel listener;
    private ExecutorService executorService;
    private List<AsynchronousSocketChannel> connections;

    public AioServer(int port, ExecutorService executorService) {
        this.port = port;
        this.executorService = executorService;
        this.logger = LogManager.getLogger(getClass());
        this.connections = Collections.synchronizedList(new LinkedList<AsynchronousSocketChannel>());
    }

    public void startListening(Dispatcher dispatcher) {
        try {
            AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(this.executorService);
            this.listener = AsynchronousServerSocketChannel.open(channelGroup);
            this.listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            this.listener.bind(new InetSocketAddress(this.port));
            this.acceptRequests(dispatcher);
        } catch (IOException exception) {
            logger.error("failed to start the listener due to io error", exception);
        }
    }

    public void stop() {
        for (AsynchronousSocketChannel connection : this.connections) {
            try {
                connection.close();
            } catch (IOException error) {
                logger.info("failed to close connection, but it's fine", error);
            }
        }

        if (this.listener != null) {
            try {
                this.listener.close();
            } catch (IOException e) {
                logger.info("failed to close the listener socket", e);
            }

            this.listener = null;
        }

        if (this.executorService != null) {
            this.executorService.shutdown();
            this.executorService = null;
        }
    }

    private void acceptRequests(Dispatcher dispatcher) {
        try {
            this.listener.accept(dispatcher, AsyncUtility.handlerFrom(
                    (AsynchronousSocketChannel connection, Dispatcher handler) -> {
                        connections.add(connection);
                        acceptRequests(handler);
                        readRequest(connection, handler);
                    },
                    (Throwable error, Dispatcher handler) -> {
                        logger.error("accepting a new connection failed, will still keep accepting more requests", error);
                        acceptRequests(handler);
                    }));
        } catch (Exception exception) {
            logger.error("failed to accept new requests, will retry", exception);
            this.acceptRequests(dispatcher);
        }
    }

    private void readRequest(final AsynchronousSocketChannel connection, Dispatcher dispatcher) {
        ByteBuffer buffer = ByteBuffer.allocate(ByteUtil.intLen);
        try {
            AsyncUtility.readFromChannel(connection, buffer, dispatcher, handlerFrom((Integer bytesRead, final Dispatcher handler) -> {
                if (bytesRead.intValue() < ByteUtil.intLen) {
                    logger.info("failed to read the request header from client socket");
                    closeSocket(connection);
                } else {
                    try {
                        logger.debug("request header read, try to see if there is a request body");
                        byte[] lenData = buffer.array();
                        int len = ByteUtil.getLowInt(lenData);
                        if (len > 0) {
                            ByteBuffer bodyBuffer = ByteBuffer.allocate(len);
                            AsyncUtility.readFromChannel(connection, bodyBuffer, null, handlerFrom((Integer size, Object attachment) -> {
                                if (size.intValue() < len) {
                                    logger.info("failed to read the log entries data from client socket");
                                    closeSocket(connection);
                                } else {
                                    try {
                                        byte[] dataByte = bodyBuffer.array();
                                        processRequest(connection, dataByte, handler);
                                    } catch (Throwable error) {
                                        logger.info("log entries parsing error", error);
                                        closeSocket(connection);
                                    }
                                }
                            }, connection));
                        } else {
                            readRequest(connection, handler);
                        }
                    } catch (Throwable runtimeError) {
                        // if there are any conversion errors, we need to close the client socket to prevent more errors
                        closeSocket(connection);
                        logger.info("message reading/parsing error", runtimeError);
                    }
                }
            }, connection));
        } catch (Exception readError) {
            logger.info("failed to read more request from client socket", readError);
            closeSocket(connection);
        }
    }

    private void processRequest(AsynchronousSocketChannel connection, byte[] dataByte, Dispatcher dispatcher) {
        try {
            byte[] responseBytes = dispatcher.processByte(dataByte);
            final ByteBuffer buffer = ByteBuffer.wrap(responseBytes);
            AsyncUtility.writeToChannel(connection, buffer, null, handlerFrom((Integer bytesSent, Object attachment) -> {
                if (bytesSent.intValue() < buffer.limit()) {
                    logger.info("failed to completely send the response.");
                    closeSocket(connection);
                } else {
                    logger.debug("response message sent.");
                    if (connection.isOpen()) {
                        logger.debug("try to read next request");
                        readRequest(connection, dispatcher);
                    }
                }
            }, connection));
        } catch (Throwable error) {
            // for any errors, we will close the socket to prevent more errors
            closeSocket(connection);
            logger.error("failed to process the request or send the response", error);
        }
    }

    private <V, A> CompletionHandler<V, A> handlerFrom(BiConsumer<V, A> completed, AsynchronousSocketChannel connection) {
        return AsyncUtility.handlerFrom(completed, (Throwable error, A attachment) -> {
            this.logger.info("socket server failure", error);
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
