package com;

import java.util.Arrays;

public class IndexSearch {

    public int search(int v, int[] a) {
        int right = 0;
        int left = a.length - 1;

        while (right <= left) {
            int mid = (right + left) / 2;
            if (a[mid] < v) {
                right = mid + 1;
            } else if (a[mid] > v) {
                left = mid - 1;
            } else {
                int i = 1;
                while (mid+i < a.length && a[mid] == a[mid + i]) {
                    i++;
                }
                return mid + (i - 1);
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int [] a = {1,2,3,4,4,4,4,7,8,9,11};
        int search = new IndexSearch().search(9, a);
    }
}
