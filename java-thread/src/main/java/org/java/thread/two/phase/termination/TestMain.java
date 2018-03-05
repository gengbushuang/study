package org.java.thread.two.phase.termination;

public class TestMain {
	public static void main(String[] args) {
		System.out.println("main:BEGIN");

		try {
			CountupThread thread = new CountupThread();
			thread.start();
			Thread.sleep(10000);
			System.out.println("main:shutdownRequest");
			thread.shutdownRequest();
			System.out.println("main:join");
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("main:END");
	}
}
