package org.java.thread.thread.per.message;

import java.util.concurrent.ThreadFactory;

public class HostThreadFactory {
	final Helper helper = new Helper();
	private final ThreadFactory factory;

	public HostThreadFactory(ThreadFactory factory) {
		this.factory = factory;
	}

	public void request(final int count, final char c) {
		System.out.println("     request(" + count + ", " + c + ") BEGIN");
		factory.newThread(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		}).start();
		System.out.println("     request(" + count + ", " + c + ") END");
	}
}
