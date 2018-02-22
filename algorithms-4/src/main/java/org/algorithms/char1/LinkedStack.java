package org.algorithms.char1;

import java.util.Iterator;
/**
 * 链表栈
 * @author gbs
 *
 * @param <T>
 */
public class LinkedStack<T> implements Iterable<T> {

	private int n;

	private Node first;

	private class Node {
		private T item;
		private Node next;
	}
	
	public LinkedStack() {
		this.n = 0;
	}

	public void push(T t) {
		Node oldFirst = first;
		first = new Node();
		first.item = t;
		first.next = oldFirst;
		n++;
	}

	public T pop() {
		if (isEmpty()) {
			return null;
		}
		T tmp = first.item;
		first = first.next;
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
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void main(String[] args) {
		LinkedStack<String> stack = new LinkedStack<>();
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
	}

}
