package com;

import java.util.ArrayList;
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
       Object  arrayList = new ArrayList();

       System.out.println(arrayList.toString().trim());
    }
}
