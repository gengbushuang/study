package org.algorithms.char1;

import java.util.Iterator;

/**
 * 下压(后进先出,LIFO)栈
 * 
 * @author gbs
 *
 * @param <T>
 */
public class Stack<T> implements Iterable<T> {

	private Object[] ts;

	private int n;

	public Stack() {
		this(2);
	}

	public Stack(int cap) {
		ts = new Object[cap];
		this.n = 0;
	}

	public void push(T t) {
		if (n == ts.length) {
			resize(2 * n);
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

	public T pop() {
		T t = (T) ts[--n];
		ts[n] = null;
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
		return new StackArrayIterator();
	}

	private class StackArrayIterator implements Iterator<T> {
		private int i = n;

		@Override
		public boolean hasNext() {
			return i > 0;
		}

		@Override
		public T next() {
			return (T) ts[--i];
		}

	}

	public static void main(String[] args) {
		Stack<String> stack = new Stack<>();
		String itemStr = "to be or not to - be - - that - - - is";
		String[] items = itemStr.split(" ");
		for (String item : items) {
			if (!item.equals("-")) {
				stack.push(item);
			} else if (!stack.isEmpty()) {
				System.out.print(stack.pop() + " ");
			}
		}
		System.out.println("(" + stack.size() + " left on stack)");
		
		for(String item:stack) {
			System.out.println(item);
		}
	}
}
