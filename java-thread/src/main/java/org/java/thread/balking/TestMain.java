package org.java.thread.balking;

public class TestMain {

	public static void main(String[] args) {
		Data data = new Data("data.txt", "(empty)");
		new ChangerThread("ChangerThread", data).start();;
		new SaverThread("SaverThread", data).start();;
	}
}
