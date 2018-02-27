package org.java.thread.producer.consumer;

import java.util.Random;

/**
 * 客人取桌子上的蛋糕吃
 * 
 * @author gbs
 *
 */
public class EaterThread extends Thread {

	private final Table table;

	private final Random random;

	public EaterThread(String name, Table table, long seed) {
		super(name);
		this.table = table;
		this.random = new Random(seed);
	}

	@Override
	public void run() {
		try {
			while (true) {
				table.take();
				Thread.sleep(random.nextInt(1000));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
