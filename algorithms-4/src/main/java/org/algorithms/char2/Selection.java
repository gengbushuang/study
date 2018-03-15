package org.algorithms.char2;
/**
 * 选择排序
 * @author gbs
 *
 */
public class Selection extends Example {

	public static void sort(Comparable[] a) {
		int n = a.length;
		for (int i = 0; i < n; i++) {
			int min = i;//找到最小元素的索引，从左到右依次替换
			for (int j = i + 1; j < n; j++) {
				if (less(a[j], a[min])) {
					min = j;
				}
			}
			exch(a, i, min);
		}
	}
}
