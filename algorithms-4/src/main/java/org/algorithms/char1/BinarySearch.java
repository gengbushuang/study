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
		long n1 = System.currentTimeMillis();
		int[] a = Utils.readAllInts("largeW.txt");
		System.out.println("largew文件"+(a.length)+"条加载"+(System.currentTimeMillis()-n1)+"毫秒");
		n1 = System.currentTimeMillis();
		
		Arrays.sort(a);
		System.out.println("largew排序"+(System.currentTimeMillis()-n1)+"毫秒");
		n1 = System.currentTimeMillis();
		
		int[] readAllInts = Utils.readAllInts("largeT.txt");
		System.out.println("largeT文件"+(readAllInts.length)+"条加载"+(System.currentTimeMillis()-n1)+"毫秒");
		n1 = System.currentTimeMillis();
		
		for (int key : readAllInts) {
			if (rank(key, a) < 0) {
				//System.out.println(key);
			}
		}
		System.out.println(System.currentTimeMillis()-n1+"毫秒");

	}
}
