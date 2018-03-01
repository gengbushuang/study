package org.java.thread.thread.per.message.exercises;

public class MyFrameService {
	volatile static boolean isBlak = false;

	public synchronized static void service() {
		if (isBlak) {
			return;
		}
		System.out.println("service");
		isBlak = true;
		new Thread() {
			public void run() {
				doService();
			}
		}.start();

	}

	private static void doService() {
		try {

			for (int i = 0; i < 50; i++) {
				System.out.print(".");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("done.");
		} finally {
			isBlak = false;
		}
	}
}
