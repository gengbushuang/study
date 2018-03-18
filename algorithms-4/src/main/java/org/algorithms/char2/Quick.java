package org.algorithms.char2;

public class Quick extends Example {

	public static void sort(Comparable[] a) {
		sort(a, 0, a.length - 1);
	}

	private static void sort(Comparable[] a, int start, int end) {
		if (start >= end) {
			return;
		}
		int mid = partition2(a, start, end);
		sort(a, start, mid - 1);
		sort(a, mid + 1, end);
	}

	private static int partition(Comparable[] a, int start, int end) {
		int left = start;
		int right = end;
		Comparable t = a[left]; //T,X
		left += 1;

		while (true) {
			
			while (less(t, a[right])) {
//				if (right == start) {
//					break;
//				}
				right--;
			}
			
			while (less(a[left], t)) {
				if (left == end) {
					break;
				}
				left++;
			}

			if (left >= right) {
				break;
			}
			exch(a, left, right);
		}
		exch(a, start, right);
		return right;
	}
	
	private static int partition2(Comparable[] a, int start, int end) {
		int left = start;
		int right = end;
		Comparable t = a[left];
		for(int i = left+1;i<=right;i++){
			if(less(a[i],t)){
				left++;
				exch(a, left, i);
			}
		}
		
		exch(a, start, left);
		return left;
	}
}
