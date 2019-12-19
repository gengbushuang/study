package org.algorithms.string;

/**
 * 字符串查找
 * bf算法
 * 暴力算法
 */
public class BruteForce {

    public int indexOf(String str1, String str2) {
        int len1=str1.length();
        int len2=str2.length();
        for (int i = 0; len1 - i >= len2; ++i) {
            int j = 0;
            for (; j < len2; ++j) {
                if (str1.charAt(i + j) != str2.charAt(j)) {
                    break;
                }
            }
            if (j == len2) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String str1 = "ababcababa";
        String str2 = "baba";
        int indexOf = new BruteForce().indexOf(str1, str2);
        System.out.println(indexOf);
    }

}
