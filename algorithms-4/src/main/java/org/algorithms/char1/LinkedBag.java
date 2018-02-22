package org.algorithms.char1;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * 链表背包
 * @author gbs
 *
 * @param <T>
 */
public class LinkedBag<T> implements Iterable<T> {

	private Node first;
	private int n;

	private class Node {
		private T item;
		private Node next;
	}

	public LinkedBag() {
		this.n = 0;
	}

	public void add(T t) {
		Node oldFirst = first;
		first = new Node();
		first.item = t;
		first.next = oldFirst;
		n++;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedBagIterator();
	}

	private class LinkedBagIterator implements Iterator<T> {

		private Node current;

		public LinkedBagIterator() {
			current = first;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T item = current.item;
			current = current.next;
			return item;
		}

	}

	public static void main(String[] args) {
		LinkedBag<String> bag = new LinkedBag<>();
		String itemStr = "to be or not to - be - - that - - - is";
		String[] items = itemStr.split(" ");
		for (String item : items) {
			bag.add(item);
		}

		for (String s : bag) {
			System.out.println(s);
		}
	}

}
