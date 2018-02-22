package org.introduction.to.algorithms.sort;

import java.util.Arrays;


public class MergesSort {

	
	public void merge_sort(int [] a,int p,int r){
		if(p<r){
			int q = (p+r)/2;
			merge_sort(a,p,q);
			merge_sort(a,q+1,r);
			//System.out.println(p+","+q+","+r);
			merge(a, p, q, r);
		}
	}
	
	public void merge(int a[], int p, int q, int r) {
		int n1 = q - p + 1;
		int n2 = r - q;

		int[] li = new int[n1 + 1];
		li[n1] = Integer.MAX_VALUE;
		int[] ri = new int[n2 + 1];
		ri[n2] = Integer.MAX_VALUE;
		for (int i = 0; i < n1; i++) {
			li[i] = a[p + i];
		}
		// System.out.println("li-->" + Arrays.toString(li));

		for (int i = 0; i < n2; i++) {
			ri[i] = a[q + i + 1];
		}
		// System.out.println("ri-->" + Arrays.toString(ri));
		int left = 0, rigth = 0;
		int k = p;
		for (; k < r;) {
			if (li[left] <= ri[rigth]) {
				a[k] = li[left];
				left++;
			} else {
				a[k] = ri[rigth];
				rigth++;
			}
			k++;
		}
		if (left < rigth) {
			a[k] = li[left];
		} else {
			a[k] = ri[rigth];
		}
		// System.out.println(Arrays.toString(a));
	}

	public static void main(String[] args) {
		int[] a = { 10, 11, 23, 27, 18, 19, 14, 22, 1, 2, 3, 4, 5, 6, 7, 8 };
		MergesSort mergesSort = new MergesSort();
//		mergesSort.merge(a, 8, 11, 15);
		mergesSort.merge_sort(a, 0, a.length-1);
		System.out.println(Arrays.toString(a));
	}
}
