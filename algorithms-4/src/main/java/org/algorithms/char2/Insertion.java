package org.algorithms.char2;

/**
 * 插入排序
 * 
 * @author gbs
 *
 */
public class Insertion extends Example {

	public static void sort(Comparable[] a) {
		int n = a.length;
		for (int i = 1; i < n; i++) {
			//从后往前插入比它小的元素，跟打扑克摸牌排序一个原理
			for (int j = i; j > 0 && (less(a[j], a[j - 1])); j--) {
				exch(a, j, j - 1);
			}
		}
	}
}
