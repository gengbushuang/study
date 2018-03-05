package org.java.thread.two.phase.termination;

public class CountupThread extends Thread {

	private long counter = 0;

	private volatile boolean shutdownRequested = false;

	public void shutdownRequest() {
		shutdownRequested = true;
		interrupt();
	}

	@Override
	public void run() {

		try {
			while (!shutdownRequested) {
				doWork();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			doShutdown();
		}
	}

	private void doWork() throws InterruptedException {
		counter++;
		System.out.println("doWork:counter=" + counter);
		Thread.sleep(500);
	}

	private void doShutdown() {
		System.out.println("doShutdown:counter=" + counter);
	}

}
