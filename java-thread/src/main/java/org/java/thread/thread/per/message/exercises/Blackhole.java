package org.java.thread.thread.per.message.exercises;

public class Blackhole {
	public static void enter(Object obj) {
		System.out.println("Step 1");
		magic(obj);
		System.out.println("Step 2");
		synchronized (obj) {
			System.out.println("Step 3 (never reached here)");
		}
	}

	public static void magic(Object obj) {
		Thread thread = new Thread() {
			public void run() {
				synchronized (obj) {
					synchronized (this) {
						this.notify();
					}
					try {
						Thread.sleep(Integer.MAX_VALUE);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
		synchronized (thread) {
			try {
				thread.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		System.out.println("BEGIN");
		Object obj = new Object();
		Blackhole.enter(obj);
		System.out.println("END");
	}
}
