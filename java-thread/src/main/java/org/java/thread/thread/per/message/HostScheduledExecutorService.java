package org.java.thread.thread.per.message;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HostScheduledExecutorService {
	final Helper helper = new Helper();
	final ScheduledExecutorService scheduledExecutorService;

	public HostScheduledExecutorService(ScheduledExecutorService scheduledExecutorService) {
		this.scheduledExecutorService = scheduledExecutorService;
	}

	public void request(final int count, final char c) {
		System.out.println("     request(" + count + ", " + c + ") BEGIN");
		scheduledExecutorService.schedule(new Runnable() {
			public void run() {
				helper.handle(count, c);
			}
		}, 3, TimeUnit.SECONDS);

		System.out.println("     request(" + count + ", " + c + ") END");
	}
}
