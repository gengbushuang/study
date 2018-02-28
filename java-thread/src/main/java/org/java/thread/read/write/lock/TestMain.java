package org.java.thread.read.write.lock;

public class TestMain {
	public static void main(String[] args) {
		Data data = new Data(10);
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();
		new ReaderThread(data).start();

		new WriterThread(data, "ABCDEFGHIJKLMNOPQRSTUVWXYZ").start();
		new WriterThread(data, "abcdefghijklmnopqrstuvwxyz").start();
	}
}
