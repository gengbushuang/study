package org.java.thread.future;

import java.util.concurrent.Callable;

public class HostJavaFuture {

	public Data request(final int count, final char c) {
		System.out.println("   request(" + count + ", " + c + ") BEGIN");
		FutureDataJavaFuture futureData = new FutureDataJavaFuture(new Callable<RealData>() {

			@Override
			public RealData call() throws Exception {
				return new RealData(count, c);
			}
		});
		new Thread(futureData).start();
		System.out.println("   request(" + count + ", " + c + ") END");
		return futureData;
	}
}
