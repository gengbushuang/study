package com.wand;

public class HeapPQ<Key extends Comparable<Key>> {

	private Key[] ks;

	private int size;

	public HeapPQ(int max) {
		this.ks = (Key[]) new Comparable[max];
		this.size = 0;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	/**
	 * 插入新节点后，子节点不能比父节点大，不然要依次调整
	 *
	 * @param k
	 */
	public void insert(Key k) {
		if (size == ks.length) {
			resize(size * 2);
		}
		size += 1;
		ks[size] = k;
		swim(size);
	}

	/**
	 * 由下至上的堆序列化，子节点不能比父节点大
	 *
	 * @param n
	 */
	private void swim(int n) {
		int c = n;
		int p = c / 2;
		while (p >= 1 && less(p, c)) {
			exch(p, c);
			c = p;
			p = c / 2;
		}
	}

	/**
	 * 获取根节点的最大数，在把最后节点跟根节点交换，父节点不能比子节点小，不然依次调整
	 *
	 * @return
	 */
	public Key delMax() {
		if (isEmpty()) {
			return null;
		}
		Key k = ks[1];
		exch(1, size);
		ks[size] = null;
		size -= 1;
		sink(1);
		return k;
	}

	/**
	 * 由上至下的堆序列化，父节点不能比子节点小
	 *
	 * @param n
	 */
	private void sink(int n) {
		int p = n;
		int c = n * 2;
		while (c <= size) {
			if (c < size && less(c, c + 1)) {
				c++;
			}
			if (!less(p, c)) {
				break;
			}
			exch(c, p);
			p = c;
			c = p * 2;
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

	private void resize(int max) {
		Key[] obs = (Key[]) new Comparable[max];
		for (int i = 0; i < size; i++) {
			obs[i] = ks[i];
		}
		this.ks = obs;
	}


}