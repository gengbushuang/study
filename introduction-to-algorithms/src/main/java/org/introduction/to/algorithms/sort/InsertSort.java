package org.introduction.to.algorithms.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 插入排序 对于少量元素的排序，它是一个有效的算法。插入排序的工作方式像许多人排序一手扑克牌。
 * 开始时，我们的左手为空并且桌子上的牌面向下。然后，我们每次从桌子上拿走一张牌并将它插入左手中正确的位置。
 * 为了找到一张牌的正确位置，我们从右到左将它与已在手中的每张牌进行比较
 * 
 * @author gbs
 *
 */
public class InsertSort {

	public void sort(int[] i) {
		int len = i.length;
		int n;
		int key;
		for (int j = 1; j < len; j++) {
			key = i[j];
			n = j - 1;
			while (n >= 0 && i[n] > key) {
				i[n + 1] = i[n];
				n -= 1;
			}
			i[n+1] = key;
		}
	}

	public int[] createArray(int n, int range) {
		int[] tmp = new int[n];
		Random r = new Random();
		for (int i = 0; i < n; i++) {
			tmp[i] = r.nextInt(range);
		}
		return tmp;
	}

	public void show(int[] tmp) {
		System.out.println(Arrays.toString(tmp));
	}

	public static void main(String[] args) {
		InsertSort insertSort = new InsertSort();
		int[] array = insertSort.createArray(10, 150);
		insertSort.show(array);
		insertSort.sort(array);
		insertSort.show(array);
	}
}
