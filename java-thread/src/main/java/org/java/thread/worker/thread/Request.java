package org.java.thread.worker.thread;

import java.util.Random;

/**
 * 工作请求的类
 * 
 * @author gbs
 *
 */
public class Request {

	private final int number;
	private final String name;
	private static final Random random = new Random();

	public Request(int number, String name) {
		this.name = name;
		this.number = number;
	}

	public void execute() {
		System.out.println(Thread.currentThread().getName() + " executes " + this);

		try {
			Thread.sleep(random.nextInt(1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "[Request No." + number + " from " + name + "]";
	}
}
