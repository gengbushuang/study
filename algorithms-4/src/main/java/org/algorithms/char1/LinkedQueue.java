package org.algorithms.char1;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 链表队列
 * 
 * @author gbs
 *
 * @param <T>
 */
public class LinkedQueue<T> implements Iterable<T> {

	private Node first;

	private Node last;

	private int n;

	private class Node {
		private T item;
		private Node next;
	}

	public LinkedQueue() {
		this.n = 0;
	}

	public void enqueue(T t) {
		Node oldLast = last;
		last = new Node();
		last.item = t;
		if (first == null) {
			first = last;
		} else {
			oldLast.next = last;
		}
		n++;
	}

	public T dequeue() {

		if (isEmpty()) {
			return null;
		}
		
		T tmp = first.item;
		first = first.next;
		if (isEmpty()) {
			last = null;
			return null;
		}
		n--;
		return tmp;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public int size() {
		return n;
	}

	@Override
	public Iterator<T> iterator() {
		return new QueueLinkedIterator();
	}

	private class QueueLinkedIterator implements Iterator<T> {

		private Node current;

		public QueueLinkedIterator() {
			this.current = first;
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
		LinkedQueue<String> queue = new LinkedQueue<>();
		String itemStr = "to be or not to - be - - that - - - is";
		String[] items = itemStr.split(" ");
		for (String item : items) {
			queue.enqueue(item);
		}

		for (String s : queue) {
			System.out.println(s);
		}
	}
}
