package network.aio;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.data.technology.jraft.extensions.AsyncUtility;
import network.message.MessageHandler;

public class Service {
	// 保存一些连接
	private List<AsynchronousSocketChannel> connections;

	private ExecutorService executorService;

	private int port;

	private AsynchronousServerSocketChannel listener;

	private Logger logger;

	public Service(int port, ExecutorService executorService) {
		this.logger = LogManager.getLogger(getClass());
		this.port = port;
		// 线程池
		this.executorService = executorService;
		// 创建同步list队列
		this.connections = Collections.synchronizedList(new LinkedList<AsynchronousSocketChannel>());
	}

	public void startListening(MessageHandler messageHandler) {
		try {
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(this.executorService);
			this.listener = AsynchronousServerSocketChannel.open(group);
			this.listener.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			// 监听端口
			this.listener.bind(new InetSocketAddress(this.port));
			this.acceptRequests(messageHandler);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void acceptRequests(MessageHandler messageHandler) {
		try {
			// 先建立连接
			this.listener.accept(messageHandler, AsyncUtility.handlerFrom((socketchannel, message) -> {
				connections.add(socketchannel);
				acceptRequests(message);
				readRequest(socketchannel, message);
			}, (error, message) -> {
				logger.error("accepting a new connection failed, will still keep accepting more requests", error);
				acceptRequests(message);
			}));
		} catch (Exception exception) {
			logger.error("failed to accept new requests, will retry", exception);
			this.acceptRequests(messageHandler);
		}
	}

	private void readRequest(AsynchronousSocketChannel socketchannel, MessageHandler messageHandler) {
		ByteBuffer buffer = ByteBuffer.allocate(10);
		
		AsyncUtility.readFromChannel(socketchannel, buffer, messageHandler,handlerFrom((Integer bytesRead,final MessageHandler message)->{
			if(bytesRead.intValue()){
				logger.info("failed to read the request header from client socket");
				closeSocket(socketchannel);
			}else{
				
			}
			
		}, socketchannel));
	}
	
	 private <V, A> CompletionHandler<V, A> handlerFrom(BiConsumer<V, A> completed, AsynchronousSocketChannel connection) {
	        return AsyncUtility.handlerFrom(completed, (Throwable error, A attachment) -> {
	                        this.logger.info("socket server failure", error);
	                        if(connection != null){
	                            closeSocket(connection);
	                        }
	                    });
	    }

	//关闭
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
