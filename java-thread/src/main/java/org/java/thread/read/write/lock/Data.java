package org.java.thread.read.write.lock;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 可以读写的类
 * 
 * @author gbs
 *
 */
public class Data {
	final char[] buffer;
	final ReadWriterLock readWriterLock = new ReadWriterLock();

	// java自带的
	// final ReadWriteLock lock = new ReentrantReadWriteLock(false);
	// final Lock readLock = lock.readLock();
	// final Lock writeLock = lock.writeLock();

	public Data(int size) {
		buffer = new char[size];
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = '*';
		}
	}

	public char[] read() throws InterruptedException {
		readWriterLock.readLock();
		// readLock.lock();
		try {
			return doRead();
		} finally {
			// readLock.unlock();
			readWriterLock.readUnlock();
		}
	}

	private char[] doRead() {
		// char[] newBuf = new char[buffer.length];
		// System.arraycopy(buffer, 0, newBuf, 0, buffer.length);
		char[] newBuf = Arrays.copyOf(buffer, buffer.length);

		slowly();
		return newBuf;
	}

	public void write(char c) throws InterruptedException {
		readWriterLock.writeLock();
		// writeLock.lock();
		try {
			doWrite(c);
		} finally {
			// writeLock.unlock();
			readWriterLock.writeUnlock();
		}
	}

	private void doWrite(char c) {
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = c;
			slowly();
		}
	}

	private void slowly() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
