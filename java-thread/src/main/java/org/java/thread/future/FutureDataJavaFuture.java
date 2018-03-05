package org.java.thread.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureDataJavaFuture extends FutureTask<RealData> implements Data {

	public FutureDataJavaFuture(Callable<RealData> callable) {
		super(callable);
	}

	@Override
	public String getContent() {
		try {
			return get().getContent();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

}
