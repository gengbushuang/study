package org.algorithms.char2;
/**
 * 合并排序
 * @author gbs
 *
 */
public class Merage extends Example {

	private static Comparable[] aux;

	public static void sort(Comparable[] a) {
		aux = new Comparable[a.length];
		sort(a, 0, a.length - 1);
	}

	private static void sort(Comparable[] a, int start, int end) {
		if (start >= end) {
			return;
		}
		int mid = (end + start) / 2;
		//先排序左边
		sort(a, start, mid);
		sort(a, mid + 1, end);
		merage(a, start, mid, end);
	}

	private static void merage(Comparable[] a, int start, int mid, int end) {
		int i = start;
		int j = mid + 1;
		for (int k = start; k <= end; k++) {
			aux[k] = a[k];
		}

		for (int k = start; k <= end; k++) {
			//左边大于中间
			if (i > mid) {
				a[k] = aux[j++];
			} else if (j > end) {
				a[k] = aux[i++];
			//左边第一个和中间第一个开始比大小
			} else if (less(aux[i], aux[j])) {
				a[k] = aux[i++];
			} else {
				a[k] = aux[j++];
			}
		}

	}
}
