package org.algorithms.char2;

/**
 * 希尔排序
 * 
 * @author gbs
 *
 */
public class Shell extends Example {

	public static void sort(Comparable[] a) {
		int n = a.length;
		int h = 1;
		// 先找到h的间隔
		while (h < n / 3) {
			h = h * 3 + 1;
		}
		while (h >= 1) {
			for (int i = h; i < n; i++) {
				//h间隔之间的数组插入排序
				for (int j = i; j >= h && (less(a[j], a[j - h])); j -= h) {
					exch(a, j, j - h);
				}
			}
			h = h / 3;
		}
	}
}
