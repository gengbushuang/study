package org.raft.common.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RpcTcpService {

	private int port;

	private AsynchronousServerSocketChannel listener;
	private ExecutorService executorService;

	private Logger logger;

	public RpcTcpService(int listeningPort) {
		this.port = listeningPort;
		this.logger = LogManager.getLogger(getClass());
		int processors = Runtime.getRuntime().availableProcessors();
		executorService = Executors.newFixedThreadPool(processors);
	}

	public void run() {
		try {
			AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
			this.listener = AsynchronousServerSocketChannel.open(channelGroup);
			this.listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			this.listener.bind(new InetSocketAddress(this.port));
			this.acceptRequests();
		} catch (IOException exception) {
			logger.error("failed to start the listener due to io error", exception);
		}
	}

	private void acceptRequests() {

		CompletionHandler<AsynchronousSocketChannel, Object> handler = new CompletionHandler<AsynchronousSocketChannel, Object>() {

			@Override
			public void failed(Throwable error, Object attachment) {
				logger.error("accepting a new connection failed, will still keep accepting more requests", error);
				acceptRequests();
			}

			@Override
			public void completed(AsynchronousSocketChannel connection, Object attachment) {
				readRequest(connection);
				acceptRequests();
			}
		};
		try {
			this.listener.accept(null, handler);
		} catch (Exception exception) {
			logger.error("failed to accept new requests, will retry", exception);
			this.acceptRequests();
		}
	}

	private void readRequest(AsynchronousSocketChannel connection) {
		ByteBuffer buffer = ByteBuffer.allocate(4);

		CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {

			@Override
			public void failed(Throwable error, Object attachment) {
				logger.info("socket server failure", error);
				if (connection != null) {
					closeSocket(connection);
				}
			}

			@Override
			public void completed(Integer result, Object attachment) {
				if (result.intValue() < 4) {
					logger.info("failed to read the request header from client socket");
					closeSocket(connection);
				} else {
					try {
						logger.debug("request header read, try to read the message");
						int bodySize = 0;
						for (int i = 0; i < 4; ++i) {
							int value = buffer.get(i);
							bodySize = bodySize | (value << (i * 8));
						}
						if (bodySize > 1024) {
							sendResponse(connection, "Bad Request");
							return;
						}
						ByteBuffer bodyBuffer = ByteBuffer.allocate(bodySize);
						readBody(connection, bodyBuffer);
					} catch (Throwable runtimeError) {
						closeSocket(connection);
						logger.info("message reading/parsing error", runtimeError);
					}
				}
			}
		};

		readFromChannel(connection, buffer, null, handler);
	}

	private void sendResponse(AsynchronousSocketChannel connection, String message) {
		byte[] resp = message.getBytes(StandardCharsets.UTF_8);
		int respSize = resp.length;
		ByteBuffer respBuffer = ByteBuffer.allocate(respSize + 4);
		for (int i = 0; i < 4; ++i) {
			int value = (respSize >> (i * 8));
			respBuffer.put((byte) (value & 0xFF));
		}
		respBuffer.put(resp);
		respBuffer.flip();

		CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {

			@Override
			public void failed(Throwable error, Object attachment) {
				logger.info("socket server failure", error);
				if (connection != null) {
					closeSocket(connection);
				}
			}

			@Override
			public void completed(Integer bytesWrite, Object attachment) {
				if (bytesWrite < respBuffer.limit()) {
					logger.info("failed to write all data back to response channel");
					closeSocket(connection);
				} else {
					readRequest(connection);
				}

			}
		};

		try {
			writeToChannel(connection, respBuffer, null, handler);
		} catch (Exception writeError) {
			logger.info("failed to write response to client socket", writeError);
			closeSocket(connection);
		}

	}

	public void writeToChannel(AsynchronousByteChannel channel, ByteBuffer buffer, Object attachment, CompletionHandler<Integer, Object> completionHandler) {
		CompletionHandler<Integer, AsyncContext<Object>> handler = new CompletionHandler<Integer, RpcTcpService.AsyncContext<Object>>() {

			@Override
			public void completed(Integer result, AsyncContext<Object> a) {
				int bytesRead = result.intValue();
				if (bytesRead == -1 || !buffer.hasRemaining()) {
					a.completionHandler.completed(buffer.position(), a.attachment);
				} else {
					writeToChannel(channel, buffer, a.attachment, a.completionHandler);
				}
			}

			@Override
			public void failed(Throwable error, AsyncContext<Object> a) {
				a.completionHandler.failed(error, a.attachment);
			}
		};

		try {
			channel.write(buffer, new AsyncContext<Object>(attachment, completionHandler), handler);
		} catch (Throwable exception) {
			completionHandler.failed(exception, attachment);
		}
	}

	private void readBody(AsynchronousSocketChannel connection, ByteBuffer bodyBuffer) {
		CompletionHandler<Integer, Object> handler = new CompletionHandler<Integer, Object>() {

			@Override
			public void failed(Throwable error, Object attachment) {
				logger.info("socket server failure", error);
				if (connection != null) {
					closeSocket(connection);
				}
			}

			@Override
			public void completed(Integer bytesRead, Object attachment) {
				if (bytesRead.intValue() < bodyBuffer.limit()) {
					logger.info("failed to read the request body from client socket");
					closeSocket(connection);
				} else {
					String message = new String(bodyBuffer.array(), StandardCharsets.UTF_8);
					CompletableFuture<String> future = new CompletableFuture<String>();
					future.whenCompleteAsync((String ack, Throwable err) -> {
						if (err != null) {
							sendResponse(connection, err.getMessage());
						} else {
							sendResponse(connection, ack);
						}
					});
					processMessage(message, future);
				}

			}
		};
	}

	private void processMessage(String message, CompletableFuture<String> future) {
	     System.out.println("Got message " + message);
	}

	public void readFromChannel(AsynchronousByteChannel channel, ByteBuffer buffer, Object attachment, CompletionHandler<Integer, Object> completionHandler) {
		CompletionHandler<Integer, AsyncContext<Object>> handler = new CompletionHandler<Integer, RpcTcpService.AsyncContext<Object>>() {

			@Override
			public void completed(Integer result, AsyncContext<Object> a) {
				int bytesRead = result.intValue();
				if (bytesRead == -1 || !buffer.hasRemaining()) {
					a.completionHandler.completed(buffer.position(), a.attachment);
				} else {
					readFromChannel(channel, buffer, a.attachment, a.completionHandler);
				}
			}

			@Override
			public void failed(Throwable error, AsyncContext<Object> a) {
				a.completionHandler.failed(error, a.attachment);
			}
		};

		try {
			channel.read(buffer, new AsyncContext<Object>(attachment, completionHandler), handler);
		} catch (Throwable exception) {
			completionHandler.failed(exception, attachment);
		}
	}

	private void closeSocket(AsynchronousSocketChannel connection) {
		try {
			connection.close();
		} catch (IOException ex) {
			this.logger.info("failed to close client socket", ex);
		}
	}

	private static class AsyncContext<A> {
		private A attachment;
		private CompletionHandler<Integer, A> completionHandler;

		public AsyncContext(A attachment, CompletionHandler<Integer, A> handler) {
			this.attachment = attachment;
			this.completionHandler = handler;
		}
	}
}
