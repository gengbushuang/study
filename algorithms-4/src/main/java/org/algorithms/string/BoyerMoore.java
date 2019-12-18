package org.algorithms.string;

import java.util.regex.Matcher;

/**
 * BM算法
 */
public class BoyerMoore {
    public int indexOf(String str, String parent) {
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
            int xi = -1;
            //查找模式匹配字符里面是否有匹配到坏字符下标的字符
            for (int k = 0; k < j; k++) {
                if (str.charAt(i + j) == parent.charAt(k)) {
                    xi = k;
                    break;
                }
            }
            //向后滑动第几位
            i = i + j - xi;
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "abcacabcbabacabc";
        String parent = "abacabc";
        int indexOf = new BoyerMoore().indexOf(str, parent);
        System.out.println(indexOf);
    }
}
