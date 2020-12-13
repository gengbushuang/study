package org.algorithms.sort;

import org.algorithms.util.RandomUtil;

import java.util.Arrays;
import java.util.Random;

public class QuickSort {


    public void qsort1(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        //m代表中间值下标
        int m = left;
        for (int i = left + 1; i <= right; i++) {
            //如果右边的数小于中间数
            //就移动m下标，替换右边i下标的数据，保证m右边的数大于中间数

            //但是如果是有序的数组，每次到要进行替换
            if (a[i] < a[left]) {
                swap(a, ++m, i);
            }
        }
        swap(a, m, left);


        qsort1(a, left, m - 1);
        qsort1(a, m + 1, right);
    }

    final RandomUtil randomUtil = new RandomUtil();

    public void qsort3(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        int t = a[left];
        int l = left;
        int r = right + 1;
        for (; ; ) {
            do {
                l++;
            } while (l <= right && a[l] < t);

            do {
                r--;
            } while (a[r] > t);
            if (l > r) {
                break;
            }

            swap(a, l, r);
        }

        swap(a, left, r);
        qsort3(a, left, r - 1);
        qsort3(a, r + 1, right);
    }

    public void qsort4(int[] a, int left, int right) {
        //判断还剩多少长度数组改用插入排序
        if (right - left < 5) {
            insertionSort(a, left, right + 1);
            return;
        }
        //随机获取中间数
        int randint = randomUtil.randint(left, right);
        swap(a, left, randint);

        int t = a[left];
        int l = left;
        int r = right + 1;
        for (; ; ) {
            do {
                l++;
            } while (l <= right && a[l] < t);

            do {
                r--;
            } while (a[r] > t);
            if (l > r) {
                break;
            }

            swap(a, l, r);
        }

        swap(a, left, r);
        qsort3(a, left, r - 1);
        qsort3(a, r + 1, right);
    }

    void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i < right; i++) {
            for (int j = i; j > left && a[j - 1] > a[j]; j--) {
                swap(a, j - 1, j);
            }
        }
    }

    void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    public static void main(String[] args) {
        java.util.Random random = new Random();
        int n = 50;
        int[] a = new int[n];
        for (int j = 0; j < n; j++) {
            a[j] = random.nextInt(5000);
        }

        new QuickSort().qsort4(a, 0, a.length - 1);
        System.out.println(Arrays.toString(a));
//        new QuickSort().insertionSort(a, 0, a.length);
    }
}
