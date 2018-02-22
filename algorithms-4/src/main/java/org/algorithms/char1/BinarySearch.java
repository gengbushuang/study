package org.algorithms.char1;

import java.util.Arrays;

import org.algorithms.util.Utils;

public class BinarySearch {

	public static int rank(int key, int[] a) {
		int start = 0;
		int end = a.length - 1;
		int mind = 0;
		while (start <= end) {
			mind = (start + end) / 2;
			if (a[mind] < key) {
				start = mind + 1;
			} else if (a[mind] > key) {
				end = mind - 1;
			} else {
				return mind;
			}
		}
		return -mind;
	}

	public static void main(String[] args) {
		int[] a = Utils.readAllInts("largeW.txt");
		Arrays.sort(a);
		int[] readAllInts = Utils.readAllInts("largeT.txt");
		long n1 = System.currentTimeMillis();
		for (int key : readAllInts) {
			if (rank(key, a) < 0) {
				System.out.println(key);
			}
		}
		System.out.println(System.currentTimeMillis()-n1+"秒");

	}
}
