package org.java.thread.worker.thread;

import java.util.Random;

/**
 * 发出工作请求的线程
 * 
 * @author gbs
 *
 */
public class ClientThread extends Thread {
	private final Channel channel;
	private static final Random random = new Random();

	public ClientThread(String name, Channel channel) {
		super(name);
		this.channel = channel;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; true; i++) {
				Request request = new Request(i, getName());
				channel.putRequest(request);
				Thread.sleep(random.nextInt(1000));
			}
		} catch (InterruptedException e) {

		}
	}

}
