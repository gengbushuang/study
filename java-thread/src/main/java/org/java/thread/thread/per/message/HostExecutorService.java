package org.java.thread.thread.per.message;

import java.util.concurrent.ExecutorService;

public class HostExecutorService {
	final Helper helper = new Helper();
	private final ExecutorService executorService;

	public HostExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void request(final int count, final char c) {
		System.out.println("     request(" + count + ", " + c + ") BEGIN");
		executorService.execute(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		});
		System.out.println("     request(" + count + ", " + c + ") END");
	}
}
