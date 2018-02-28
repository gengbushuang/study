package org.java.thread.read.write.lock;

public class ReaderThread extends Thread {

	final Data data;

	public ReaderThread(Data data) {
		this.data = data;
	}

	@Override
	public void run() {

		try {
			while (true) {
				char[] read = data.read();
				System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(read));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
