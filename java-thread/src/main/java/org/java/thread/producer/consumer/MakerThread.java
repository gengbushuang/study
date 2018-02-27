package org.java.thread.producer.consumer;

import java.util.Random;

/**
 * 糕点师制作蛋糕，并将其放置到桌子上
 * 
 * @author gbs
 *
 */
public class MakerThread extends Thread {

	private final Table table;

	private final Random random;

	private static int id = 0;

	public MakerThread(String name, Table table, long seed) {
		super(name);
		this.table = table;
		this.random = new Random(seed);
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(random.nextInt(1000));
				String cake = "[ Cake No." + nextId() + " by " + getName() + "]";
				table.put(cake);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static synchronized int nextId() {
		return id++;
	}
}
