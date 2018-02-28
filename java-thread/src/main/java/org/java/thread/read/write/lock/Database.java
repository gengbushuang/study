package org.java.thread.read.write.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database<K, V> {
	final ReadWriteLock lock = new ReentrantReadWriteLock(false);
	final Lock readLock = lock.readLock();
	final Lock writeLock = lock.writeLock();
	private final Map<K, V> map = new HashMap<K, V>();

	// 全部清除
	// public synchronized void clear() {
	// verySlowly();
	// map.clear();
	// }
	public void clear() {
		writeLock.lock();
		try {
			verySlowly();
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

	// 给key分配value
	// public synchronized void assign(K key, V value) {
	// verySlowly();
	// map.put(key, value);
	// }
	public void assign(K key, V value) {
		writeLock.lock();
		try {
			verySlowly();
			map.put(key, value);
		} finally {
			writeLock.lock();
		}
	}

	// 获取给key分配的值
	// public synchronized V retrieve(K key) {
	// slowly();
	// return map.get(key);
	// }
	public V retrieve(K key) {
		readLock.lock();
		try {
			slowly();
			return map.get(key);
		} finally {
			readLock.unlock();
		}
	}

	private void slowly() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void verySlowly() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
