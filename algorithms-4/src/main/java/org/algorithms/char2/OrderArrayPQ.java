package org.algorithms.char2;

/**
 * 有序优先队列
 * 
 * @author gbs
 *
 * @param <Key>
 */
public class OrderArrayPQ<Key extends Comparable<Key>> {

	private Key[] ks;

	private int size;

	public OrderArrayPQ(int max) {
		this.ks = (Key[]) new Comparable[max];
		this.size = 0;
	}

	/**
	 * 插入的时候，先利用插入排序排好序
	 * 
	 * @param k
	 */
	public void insert(Key k) {
		if (size == ks.length) {
			resize(size * 2);
		}
		ks[size++] = k;
		sortMax();
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public Key delMax() {
		if (isEmpty()) {
			return null;
		}
		Key k = ks[--size];
		ks[size] = null;
		return k;
	}

	private void resize(int max) {
		Key[] obs = (Key[]) new Comparable[max];
		for (int i = 0; i < size; i++) {
			obs[i] = ks[i];
		}
		this.ks = obs;
	}

	private void sortMax() {
		for (int i = size - 1; i > 0; i--) {
			if (less(i, i - 1)) {
				exch(i, i - 1);
			} else {
				break;
			}
		}
	}

	private void exch(int i, int j) {
		Key t = ks[i];
		ks[i] = ks[j];
		ks[j] = t;
	}

	private boolean less(int i1, int i2) {
		return ks[i1].compareTo(ks[i2]) < 0;
	}

	public static void main(String[] args) {

	}
}
