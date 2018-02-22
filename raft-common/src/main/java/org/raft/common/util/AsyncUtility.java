package org.raft.common.util;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousByteChannel;
import java.nio.channels.CompletionHandler;

public class AsyncUtility {

	/**
	 * 监听读取
	 * 
	 * @param channel
	 * @param buffer
	 * @param attachment
	 * @param completionHandler
	 */
	public static <A> void readFromChannel(AsynchronousByteChannel channel, ByteBuffer buffer, A attachment, CompletionHandler<Integer, A> completionHandler) {
		CompletionHandler<Integer, AsyncContext<A>> handler = new CompletionHandler<Integer, AsyncUtility.AsyncContext<A>>() {
			@Override
			public void completed(Integer readByte, AsyncContext<A> a) {
				int byteSite = readByte.intValue();
				if (byteSite == -1 || !buffer.hasRemaining()) {
					// //ByteBuffer是读状态的时，position为读入多少
					a.completionHandler.completed(buffer.position(), a.attachment);
				} else {
					readFromChannel(channel, buffer, a.attachment, a.completionHandler);
				}
			}

			@Override
			public void failed(Throwable error, AsyncContext<A> a) {
				a.completionHandler.failed(error, a.attachment);
			}
		};
		try {
			channel.read(buffer, new AsyncContext<A>(attachment, completionHandler), handler);
		} catch (Throwable exception) {
			completionHandler.failed(exception, attachment);
		}
	}

	/**
	 * 监听写入
	 * 
	 * @param channel
	 * @param buffer
	 * @param attachment
	 * @param completionHandler
	 */
	public static <A> void writeToChannel(AsynchronousByteChannel channel, ByteBuffer buffer, A attachment, CompletionHandler<Integer, A> completionHandler) {

		CompletionHandler<Integer, AsyncContext<A>> handler = new CompletionHandler<Integer, AsyncUtility.AsyncContext<A>>() {

			@Override
			public void completed(Integer writeByte, AsyncContext<A> a) {
				int byteSite = writeByte.intValue();
				if (byteSite == -1 || !buffer.hasRemaining()) {
					// ByteBuffer是写状态的是，position为写入多少
					a.completionHandler.completed(buffer.position(), a.attachment);
				} else {
					writeToChannel(channel, buffer, a.attachment, a.completionHandler);
				}
			}

			@Override
			public void failed(Throwable error, AsyncContext<A> a) {
				a.completionHandler.failed(error, a.attachment);
			}
		};

		try {
			channel.write(buffer, new AsyncContext<A>(attachment, completionHandler), handler);
		} catch (Throwable exception) {
			completionHandler.failed(exception, attachment);
		}
	}

	static class AsyncContext<A> {
		private A attachment;
		private CompletionHandler<Integer, A> completionHandler;

		public AsyncContext(A attachment, CompletionHandler<Integer, A> handler) {
			this.attachment = attachment;
			this.completionHandler = handler;
		}
	}
}
