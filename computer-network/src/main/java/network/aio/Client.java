package network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;

import network.message.MessageType;
import network.message.RequestMessage;
import network.message.ResponseMessage;
import network.rdt.m2.Sender;
import network.rdt.model.Packet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Client {

	private AsynchronousSocketChannel connection;
	private AsynchronousChannelGroup channelGroup;

	private InetSocketAddress remote;

	private Logger logger;

	public Client(InetSocketAddress remote, ExecutorService executorService) {
		this.remote = remote;

		this.logger = LogManager.getLogger(getClass());

		try {
			this.channelGroup = AsynchronousChannelGroup.withThreadPool(executorService);
		} catch (IOException err) {

		}
	}
	Sender sender = new Sender();
	public void rtd_send(String data) throws IOException {
		int check_sum = sender.check_sum(data);
		Packet sndpkt = sender.make_pkt(data, check_sum);
		
		Packet rcvpkt = null;
		do {
			rcvpkt = udt_send(sndpkt);
		} while (sender.rdt_rcv(rcvpkt) && sender.isNAK(rcvpkt));

		if (!sender.rdt_rcv(rcvpkt) && !sender.isACK(rcvpkt)) {
			throw new IOException("ACK is ERROR!" + rcvpkt);
		}
	}

	private Packet udt_send(Packet packet) {
		RequestMessage requestMessage = new RequestMessage();
		requestMessage.setMessageType(MessageType.RequestRdt);
		requestMessage.setPacket(packet);
		CompletableFuture<ResponseMessage> send = send(requestMessage);
		try {
			ResponseMessage responseMessage = send.get();
			System.out.println(responseMessage);
			return responseMessage.getPacket();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// //////////////
	public synchronized CompletableFuture<ResponseMessage> send(final RequestMessage request) {
		CompletableFuture<ResponseMessage> result = new CompletableFuture<ResponseMessage>();

		if (this.connection == null || !this.connection.isOpen()) {
			try {
				this.connection = AsynchronousSocketChannel.open(this.channelGroup);
				this.connection.connect(remote, new AsyncTask<RequestMessage>(request, result), AsyncUtility.handlerFrom((v, task) -> {
					sendAndRead(task);
				}, (error, task) -> {
					logger.info("h1 socket error", error);
					task.future.completeExceptionally(error);
					closeSocket();
				}));
			} catch (Exception error) {
				closeSocket();
				result.completeExceptionally(error);
			}
		} else {
			this.sendAndRead(new AsyncTask<RequestMessage>(request, result));
		}
		return result;
	}

	private void sendAndRead(AsyncTask<RequestMessage> task) {
		ByteBuffer wrap = ByteBuffer.wrap(BinaryUtils.requestToByte(task.input));
		try {
			AsyncUtility.writeToChannel(connection, wrap, task, handlerFrom((Integer bytesSent, AsyncTask<RequestMessage> asyncTask) -> {
				if (bytesSent.intValue() < wrap.limit()) {
					logger.info("failed to sent the request to remote server.");
					asyncTask.future.completeExceptionally(new IOException("Only partial of the data could be sent"));
					closeSocket();
				} else {
					ByteBuffer responseBuffer = ByteBuffer.allocate(BinaryUtils.RESPONSE_HEAD_SIZE);
					this.readResponse(new AsyncTask<ByteBuffer>(responseBuffer, asyncTask.future));
				}
			}));
		} catch (Exception writeError) {
			logger.info("failed to write the socket", writeError);
			task.future.completeExceptionally(writeError);
			closeSocket();
		}
	}

	private void readResponse(AsyncTask<ByteBuffer> task) {
		try {
			this.logger.debug("reading response from socket...");
			AsyncUtility.readFromChannel(connection, task.input, task, handlerFrom((Integer bytesRead, AsyncTask<ByteBuffer> asynctask) -> {
				if (bytesRead.intValue() < BinaryUtils.RESPONSE_HEAD_SIZE) {
					logger.info("failed to read response from remote server.");
					asynctask.future.completeExceptionally(new IOException("Only part of the response data could be read"));
					closeSocket();
				} else {
					ResponseMessage byteToReponse = BinaryUtils.byteToReponse(asynctask.input.array());
					if (byteToReponse.getLength() > 0) {
						ByteBuffer packBuf = ByteBuffer.allocate(byteToReponse.getLength());
						try {
							connection.read(packBuf, asynctask, AsyncUtility.handlerFrom((Integer packRead, AsyncTask<ByteBuffer> packTask) -> {
								if (packRead.intValue() < byteToReponse.getLength()) {
									logger.info("failed to read response from remote server.");
									packTask.future.completeExceptionally(new IOException("Only part of the response data could be read"));
									closeSocket();
								} else {
									Packet byteToPacket = BinaryUtils.byteToPacket(packBuf.array());
									byteToReponse.setPacket(byteToPacket);
									packTask.future.complete(byteToReponse);
								}
							}, (error, packTask) -> {
								logger.info("failed to read response from remote server.");
								packTask.future.completeExceptionally(new IOException("Only part of the response data could be read"));
								closeSocket();
							}));
						} catch (Exception readError) {
							logger.info("failed to read from socket", readError);
							asynctask.future.completeExceptionally(readError);
							closeSocket();
						}
					} else {
						asynctask.future.complete(byteToReponse);
					}
				}
			}));
		} catch (Exception readError) {
			logger.info("failed to read from socket", readError);
			task.future.completeExceptionally(readError);
			closeSocket();
		}
	}

	private <V, I> CompletionHandler<V, AsyncTask<I>> handlerFrom(BiConsumer<V, AsyncTask<I>> completed) {
		return AsyncUtility.handlerFrom(completed, (Throwable error, AsyncTask<I> context) -> {
			this.logger.info("socket error", error);
			context.future.completeExceptionally(error);
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
		} catch (IOException ex) {
			this.logger.info("failed to close socket", ex);
		}
	}

	static class AsyncTask<TInput> {
		private TInput input;
		private CompletableFuture<ResponseMessage> future;

		public AsyncTask(TInput input, CompletableFuture<ResponseMessage> future) {
			this.input = input;
			this.future = future;
		}
	}
}
