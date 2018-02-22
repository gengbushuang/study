package org.raft.common.server;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.raft.common.message.RaftMessageHandler;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;
import org.raft.common.util.AsyncUtility;
import org.raft.common.util.BinaryUtils;
import org.raft.common.util.Pair;

public class RpcTcpListener {
	private int port;

	private Logger logger;

	private ExecutorService executorService;

	private List<AsynchronousSocketChannel> connections;

	private AsynchronousServerSocketChannel listener;

	public RpcTcpListener(int port, ExecutorService executorService) {
		this.logger = LogManager.getLogger(getClass());
		this.port = port;
		this.executorService = executorService;

		this.connections = Collections.synchronizedList(new LinkedList<AsynchronousSocketChannel>());
	}

	public void startListening(RaftMessageHandler messageHandler) {
		try {
			AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(this.executorService);
			this.listener = AsynchronousServerSocketChannel.open(channelGroup);
			this.listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			this.listener.bind(new InetSocketAddress(this.port));
			this.acceptRequests(messageHandler);
		} catch (IOException exception) {
			logger.error("failed to start the listener due to io error", exception);
		}
	}

	private void acceptRequests(RaftMessageHandler messageHandler) {

		CompletionHandler<AsynchronousSocketChannel, RaftMessageHandler> handler = new CompletionHandler<AsynchronousSocketChannel, RaftMessageHandler>() {

			@Override
			public void failed(Throwable error, RaftMessageHandler attachment) {
				logger.error("accepting a new connection failed, will still keep accepting more requests", error);
				acceptRequests(attachment);
			}

			@Override
			public void completed(AsynchronousSocketChannel connection, RaftMessageHandler attachment) {
				connections.add(connection);
				acceptRequests(attachment);
				readRequest(connection, attachment);

			}
		};

		try {
			this.listener.accept(messageHandler, handler);
		} catch (Exception exception) {
			logger.error("failed to accept new requests, will retry", exception);
			this.acceptRequests(messageHandler);
		}
	}

	// /
	private void readRequest(final AsynchronousSocketChannel connection, RaftMessageHandler messageHandler) {
		ByteBuffer buffer = ByteBuffer.allocate(BinaryUtils.RAFT_REQUEST_HEADER_SIZE);
		// CompletionHandler
		CompletionHandler<Integer, RaftMessageHandler> handler = new CompletionHandler<Integer, RaftMessageHandler>() {
			@Override
			public void failed(Throwable error, RaftMessageHandler attachment) {
				logger.info("socket server failure", error);
				if (connection != null) {
					closeSocket(connection);
				}
			}

			@Override
			public void completed(Integer bytesRead, RaftMessageHandler attachment) {
				if (bytesRead.intValue() < BinaryUtils.RAFT_REQUEST_HEADER_SIZE) {
					logger.info("failed to read the request header from client socket");
					closeSocket(connection);
				} else {
					final Pair<RaftRequestMessage, Integer> requestInfo = BinaryUtils.bytesToRequestMessage(buffer.array());
					if (requestInfo.getSecond().intValue() > 0) {
						ByteBuffer logBuffer = ByteBuffer.allocate(requestInfo.getSecond().intValue());
						// CompletionHandler
						CompletionHandler<Integer, Object> h = new CompletionHandler<Integer, Object>() {
							@Override
							public void failed(Throwable error, Object o) {
								logger.info("socket server failure", error);
								if (connection != null) {
									closeSocket(connection);
								}
							}

							@Override
							public void completed(Integer result, Object o) {
								if (result.intValue() < requestInfo.getSecond().intValue()) {
									logger.info("failed to read the log entries data from client socket");
									closeSocket(connection);
								} else {
									try {
										requestInfo.getFirst().setLogEntries(BinaryUtils.bytesToLogEntries(logBuffer.array()));
										processRequest(connection, requestInfo.getFirst(), attachment);
									} catch (Throwable error) {
										logger.info("log entries parsing error", error);
										closeSocket(connection);
									}
								}
							}
						};
						AsyncUtility.readFromChannel(connection, buffer, null, h);
					} else {
						processRequest(connection, requestInfo.getFirst(), attachment);
					}
				}

			}
		};
		try {
			AsyncUtility.readFromChannel(connection, buffer, messageHandler, handler);
		} catch (Exception readError) {
			logger.info("failed to read more request from client socket", readError);
			closeSocket(connection);
		}
	}

	private void processRequest(AsynchronousSocketChannel connection, RaftRequestMessage request, RaftMessageHandler messageHandler) {
		try {
			RaftResponseMessage response = messageHandler.processRequest(request);
			final ByteBuffer buffer = ByteBuffer.wrap(BinaryUtils.messageToBytes(response));

			CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {
				@Override
				public void failed(Throwable error, Object attachment) {
					logger.info("socket server failure", error);
					if (connection != null) {
						closeSocket(connection);
					}
				}

				@Override
				public void completed(Integer bytesSent, Object attachment) {
					if (bytesSent.intValue() < buffer.limit()) {
						logger.info("failed to completely send the response.");
						closeSocket(connection);
					} else {
						if (connection.isOpen()) {
							logger.debug("try to read next request");
							readRequest(connection, messageHandler);
						}
					}
				}
			};
			AsyncUtility.writeToChannel(connection, buffer, null, handler);
		} catch (Throwable error) {
			closeSocket(connection);
			logger.error("failed to process the request or send the response", error);
		}
	}

	private void closeSocket(AsynchronousSocketChannel connection) {
		try {
			this.connections.remove(connection);
			connection.close();
		} catch (IOException ex) {
			this.logger.info("failed to close client socket", ex);
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
}
