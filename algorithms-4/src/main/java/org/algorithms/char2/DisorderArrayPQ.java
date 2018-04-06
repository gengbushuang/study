package org.algorithms.char2;

/**
 * 无序优先队列
 * 
 * @author gbs
 *
 * @param <Key>
 */
public class DisorderArrayPQ<Key extends Comparable<Key>> {

	private Key[] ks;

	private int size;

	public DisorderArrayPQ(int max) {
		this.ks = (Key[]) new Comparable[max];
		this.size = 0;
	}

	public void insert(Key k) {
		if (size == ks.length) {
			resize(size * 2);
		}
		ks[size++] = k;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	/**
	 * 删除的时候,利用选择排序找到最大元素
	 * 
	 * @return
	 */
	public Key delMax() {
		if (isEmpty()) {
			return null;
		}
		findMax();
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

	private void findMax() {
		int n = size;
		int max = 0;
		for (int i = 1; i < n; i++) {
			if (less(max, i)) {
				max = i;
			}
		}
		exch(max, n - 1);
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
