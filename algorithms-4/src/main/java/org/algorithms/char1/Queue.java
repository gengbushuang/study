package org.algorithms.char1;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 先进先出(FIFO)队列
 * 
 * @author gbs
 *
 * @param <T>
 */
public class Queue<T> implements Iterable<T> {

	private Object[] ts;

	private int n;

	private int first;

	private int last;

	public Queue() {
		this(10);
	}

	public Queue(int cap) {
		ts = new Object[cap];
		this.n = this.first = this.last = 0;
	}

	public void enqueue(T t) {
		if (n == ts.length) {
			resize(n * 2);
		}
		ts[last] = t;
		last++;
		n++;
		if (last == ts.length) {
			last = 0;
		}
	}

	private void resize(int max) {
		Object[] obs = new Object[max];
		for (int i = 0; i < n; i++) {
			obs[i] = ts[i];
		}
		this.ts = obs;
		first = 0;
		last = n;
	}

	public T dequeue() {
		T t = (T) ts[first];
		ts[first] = null;
		n--;
		first++;
		if (first == ts.length) {
			first = 0;
		}
		return t;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	@Override
	public Iterator<T> iterator() {
		return new ArrayIterator();
	}

	private class ArrayIterator implements Iterator<T> {
		private int i = 0;

		public boolean hasNext() {
			return i < n;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public T next() {
			if (!hasNext())
				throw new NoSuchElementException();
			T item = (T) ts[(i + first) % ts.length];
			i++;
			return item;
		}
	}

}
