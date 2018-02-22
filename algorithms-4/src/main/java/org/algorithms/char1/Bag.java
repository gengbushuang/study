package org.algorithms.char1;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 背包
 * 
 * @author gbs
 *
 * @param <T>
 */
public class Bag<T> implements Iterable<T> {

	private Object[] ts;

	private int n;

	public Bag() {
		this(10);
	}

	public Bag(int cap) {
		ts = new Object[cap];
		this.n = 0;
	}

	public void add(T t) {
		if (n == ts.length) {
			resize(n * 2);
		}
		ts[n++] = t;
	}

	private void resize(int max) {
		Object[] obs = new Object[max];
		for (int i = 0; i < n; i++) {
			obs[i] = ts[i];
		}
		this.ts = obs;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new BagArrayIterator();
	}

	private class BagArrayIterator implements Iterator<T> {
		private int i = 0;

		@Override
		public boolean hasNext() {
			return i < n;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return (T) ts[i++];
		}

	}

	public static void main(String[] args) {
		Bag<String> bag = new Bag<>();
		bag.add("Hello");
		bag.add("World");
		bag.add("how");
		bag.add("are");
		bag.add("you");
		for (String s : bag) {
			System.out.println(s);
		}
	}
}
