package org.java.thread.producer.consumer;

/**
 * 桌子，最多放3个蛋糕
 * 
 * @author gbs
 *
 */
public class Table {

	final String[] buffer;
	int count;
	int head;
	int tail;

	public Table(int count) {
		this.buffer = new String[count];
		this.count = 0;
		this.head = 0;
		this.tail = 0;
	}

	public synchronized void put(String cake) throws InterruptedException {
		System.out.println(Thread.currentThread().getName() + " puts " + cake);
		while (count >= buffer.length) {
			wait();
		}
		buffer[tail] = cake;
		tail = (tail + 1) % buffer.length;
		count++;
		notifyAll();
	}

	public synchronized String take() throws InterruptedException {
		while (count <= 0) {
			wait();
		}
		String cake = buffer[head];
		head = (head + 1) % buffer.length;
		count--;
		notifyAll();
		System.out.println(Thread.currentThread().getName() + " takes " + cake);
		return cake;
	}

}
