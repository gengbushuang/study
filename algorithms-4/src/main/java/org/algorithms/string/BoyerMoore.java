package org.algorithms.string;

import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * BM算法
 */
public class BoyerMoore {

    private String parent;

    private int[] right;

    public BoyerMoore(String parent) {
        this.parent = parent;
        right = new int[256];
        Arrays.fill(right, -1);
        for (int i = 0; i < parent.length(); i++) {
            right[parent.charAt(i)] = i;
        }
    }

    public int indexOf(String str) {
        int len1 = str.length();
        int len2 = parent.length();
        for (int i = 0; i <= len1 - len2; ) {
            int j = len2 - 1;
            //从后往前匹配，记录不匹配的字符下标
            for (; j >= 0; --j) {
                if (str.charAt(i + j) != parent.charAt(j)) {
                    break;
                }
            }
            if (j < 0) {
                return i;
            }
            /////可以替换高效的查表方式
//            int xi = -1;
//            //查找模式匹配字符里面是否有匹配到坏字符下标的字符
//            for (int k = 0; k < j; k++) {
//                if (str.charAt(i + j) == parent.charAt(k)) {
//                    xi = k;
//                    break;
//                }
//            }
            int xi = right[str.charAt(i + j)];
            //向后滑动第几位
//            i = i + j - xi;//这个会出现负数
            i = i + Math.max(1, j - xi);
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "abcacabcbabacabc";
        String parent = "cabcabc";
//        parent="bcababab";
//        int[] suffixs = new int[parent.length()];
//        Arrays.fill(suffixs, -1);
//        System.out.println(Arrays.toString(suffixs));
//        for (int i = 0; i < parent.length(); i++) {
//            System.out.println(parent.substring(parent.length()-i));
//        }
//        str = "HERE IS A SIMPLE EXAMPLE";
//        parent = "EXAMPLE";
//        str = "here is a simple example";
//        parent = "example";
        int indexOf = new BoyerMoore(parent).indexOf(str);
        System.out.println(indexOf);
    }
}
