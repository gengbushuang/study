package org.java.thread.read.write.lock;

import java.util.Random;

public class WriterThread extends Thread {

	final Random random = new Random();
	final Data data;
	final String filler;
	int index = 0;

	public WriterThread(Data data, String filler) {
		this.data = data;
		this.filler = filler;
	}

	@Override
	public void run() {

		try {
			while (true) {
				char c = nextchar();
				data.write(c);
				Thread.sleep(random.nextInt(3000));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private char nextchar() {
		char charAt = filler.charAt(index);
		index++;
		if (index >= filler.length()) {
			index = 0;
		}
		return charAt;
	}

}
