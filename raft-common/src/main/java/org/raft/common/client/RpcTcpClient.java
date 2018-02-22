package org.raft.common.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;
import org.raft.common.util.AsyncUtility;
import org.raft.common.util.BinaryUtils;

public class RpcTcpClient {
	private AsynchronousSocketChannel connection;
	private AsynchronousChannelGroup channelGroup;
	//
	private ConcurrentLinkedQueue<AsyncTask<ByteBuffer>> readTasks;
	private ConcurrentLinkedQueue<AsyncTask<RaftRequestMessage>> writeTasks;
	//
	private AtomicInteger readers;
	private AtomicInteger writers;
	// 地址
	private InetSocketAddress remote;

	private Logger logger;

	public RpcTcpClient(InetSocketAddress remote, ExecutorService executorService) {
		this.remote = remote;

		this.logger = LogManager.getLogger(getClass());

		this.readTasks = new ConcurrentLinkedQueue<AsyncTask<ByteBuffer>>();
		this.writeTasks = new ConcurrentLinkedQueue<AsyncTask<RaftRequestMessage>>();

		this.readers = new AtomicInteger(0);
		this.writers = new AtomicInteger(0);
		try {
			this.channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
		} catch (IOException err) {

		}
	}

	public synchronized CompletableFuture<RaftResponseMessage> send(final RaftRequestMessage request) {
		CompletableFuture<RaftResponseMessage> result = new CompletableFuture<RaftResponseMessage>();
		if (this.connection == null || !this.connection.isOpen()) {
			try {
				this.connection = AsynchronousSocketChannel.open(this.channelGroup);
				this.connection.connect(remote, new AsyncTask<RaftRequestMessage>(request, result), h1);
			} catch (Throwable error) {
				closeSocket();
				result.completeExceptionally(error);
			}

		} else {
			this.sendAndRead(new AsyncTask<RaftRequestMessage>(request, result), false);
		}
		return result;
	}

	// ////////////////
	private void sendAndRead(AsyncTask<RaftRequestMessage> task, boolean skipQueueing) {
		// 为true就跳过写队列，否则就往写队列里面加入要写的任务对象
		if (!skipQueueing) {
			int writerCount = this.writers.getAndIncrement();
			if (writerCount > 0) {
				this.writeTasks.add(task);
				return;
			}
		}

		ByteBuffer buffer = ByteBuffer.wrap(BinaryUtils.messageToBytes(task.input));

		CompletionHandler<Integer, AsyncTask<RaftRequestMessage>> handler = new CompletionHandler<Integer, RpcTcpClient.AsyncTask<RaftRequestMessage>>() {

			@Override
			public void failed(Throwable error, AsyncTask<RaftRequestMessage> attachment) {
				logger.info("socket error", error);
				attachment.future.completeExceptionally(error);
				closeSocket();
			}

			@Override
			public void completed(Integer result, AsyncTask<RaftRequestMessage> attachment) {
				if (result.intValue() < buffer.limit()) {
					logger.info("failed to sent the request to remote server.");
					attachment.future.completeExceptionally(new IOException("Only partial of the data could be sent"));
					closeSocket();
				} else {
					ByteBuffer responseBuffer = ByteBuffer.allocate(BinaryUtils.RAFT_RESPONSE_HEADER_SIZE);
					readResponse(new AsyncTask<ByteBuffer>(responseBuffer, attachment.future), skipQueueing);
				}

				int waitingWriters = writers.decrementAndGet();
				if (waitingWriters > 0) {
					logger.debug("there are pending writers in queue, will try to process them");
					AsyncTask<RaftRequestMessage> pendingTask = null;
					while ((pendingTask = writeTasks.poll()) == null)
						;
					sendAndRead(pendingTask, true);
				}
			}
		};

		AsyncUtility.writeToChannel(this.connection, buffer, task, handler);

	}

	// ////////////
	private void readResponse(AsyncTask<ByteBuffer> task, boolean skipQueueing) {
		if (!skipQueueing) {
			int readerCount = this.readers.getAndIncrement();
			if (readerCount > 0) {
				this.logger.debug("there is a pending read, queue this read task");
				this.readTasks.add(task);
				return;
			}
		}

		CompletionHandler<Integer, AsyncTask<ByteBuffer>> handler = new CompletionHandler<Integer, RpcTcpClient.AsyncTask<ByteBuffer>>() {

			@Override
			public void failed(Throwable error, AsyncTask<ByteBuffer> attachment) {
				logger.info("socket error", error);
				attachment.future.completeExceptionally(error);
				closeSocket();
			}

			@Override
			public void completed(Integer result, AsyncTask<ByteBuffer> attachment) {
				if (result.intValue() < BinaryUtils.RAFT_RESPONSE_HEADER_SIZE) {
					attachment.future.completeExceptionally(new IOException("Only part of the response data could be read"));
					closeSocket();
				} else {
					RaftResponseMessage response = BinaryUtils.bytesToResponseMessage(attachment.input.array());
					attachment.future.complete(response);
				}

				int waitingReaders = readers.decrementAndGet();
				if (waitingReaders > 0) {
					logger.debug("there are pending readers in queue, will try to process them");
					AsyncTask<ByteBuffer> pendingTask = null;
					while ((pendingTask = readTasks.poll()) == null)
						;
					readResponse(pendingTask, true);
				}
			}
		};

		try {
			this.logger.debug("reading response from socket...");
			AsyncUtility.readFromChannel(this.connection, task.input, task, handler);
		} catch (Exception readError) {
			logger.info("failed to read from socket", readError);
			task.future.completeExceptionally(readError);
			closeSocket();
		}
	}

	// ////////////////////CompletionHandler
	CompletionHandler<Void, AsyncTask<RaftRequestMessage>> h1 = new CompletionHandler<Void, RpcTcpClient.AsyncTask<RaftRequestMessage>>() {

		@Override
		public void failed(Throwable error, AsyncTask<RaftRequestMessage> task) {
			logger.info("h1 socket error", error);
			task.future.completeExceptionally(error);
			closeSocket();
		}

		@Override
		public void completed(Void result, AsyncTask<RaftRequestMessage> task) {
			sendAndRead(task, false);
		}
	};

	private synchronized void closeSocket() {
		this.logger.debug("close the socket due to errors");
		try {
			if (this.connection != null) {
				this.connection.close();
				this.connection = null;
			}
		} catch (IOException ex) {
			this.logger.info("failed to close socket", ex);
		}
		while (true) {
			AsyncTask<ByteBuffer> task = this.readTasks.poll();
			if (task == null) {
				break;
			}
			task.future.completeExceptionally(new IOException("socket is closed, no more writes can be completed"));
		}
		while (true) {
			AsyncTask<RaftRequestMessage> task = this.writeTasks.poll();
			if (task == null) {
				break;
			}

			task.future.completeExceptionally(new IOException("socket is closed, no more writes can be completed"));
		}

		this.readers.set(0);
		this.writers.set(0);
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
