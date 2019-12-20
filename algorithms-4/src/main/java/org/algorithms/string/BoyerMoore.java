package org.algorithms.string;

import java.util.Arrays;

/**
 * BM算法
 */
public class BoyerMoore {

    private String parent;

    private int[] right;

    private int[] suffixs;

    public BoyerMoore(String parent) {
        this.parent = parent;
        //坏字符构建
        right = new int[256];
        Arrays.fill(right, -1);
        for (int i = 0; i < parent.length(); i++) {
            right[parent.charAt(i)] = i;
        }
        //好后缀构建
        suffixs = buildSuffixs(this.parent);
    }

    private int[] buildSuffixs(String parent) {
        int[] suffixs_tmp = new int[parent.length()];
        Arrays.fill(suffixs_tmp, -1);
        for (int i = 1; i < parent.length(); i++) {
            int len = parent.length() - i;
            String suffixWord = parent.substring(len);
//            for(int k=0;k<parent.length()-i;k++){
//                String word = parent.substring(k,k+i);
//                if(suffixWord.equals(word)) {
//                    System.out.println(String.format("匹配到的字符串：%s 起始位置：%d", word,k));
//                    suffixs[i]=k;
//                }
//            }
            for (int k = parent.length() - i; k >= i; k--) {
                String word = parent.substring(k - i, k);
                if (suffixWord.equals(word)) {
                    suffixs_tmp[i] = k - i;
                    break;
                }
            }
        }
        return suffixs_tmp;
    }

    public int indexOf(String str) {
        int len1 = str.length();
        int len2 = parent.length();
        int n = len2 - 1;
        for (int i = 0; i <= len1 - len2; ) {
            int j = n;
            //从后往前匹配，记录不匹配的字符下标
            for (; j >= 0; --j) {
                if (str.charAt(i + j) != parent.charAt(j)) {
                    break;
                }
            }
            if (j < 0) {
                return i;
            }
            int skip;
            if (j == n) {
                ///坏字符串匹配规则
                skip = j - right[str.charAt(i + j)];
            } else {
                int tmp = -1;
                //好后缀规则
                for (int k = n - j; k > 0; k--) {
                    tmp = Math.max(suffixs[k], tmp);
                }
                skip = Math.max(n - tmp, j - right[str.charAt(i + j)]);
            }
            //向后滑动第几位
            i = i + skip;
        }
        return -1;
    }

    public static void main(String[] args) {
        String str = "abcacabcbabacabc";
        String parent = "bacab";
//        str = "here is a simple example";
//        parent = "example";
        int indexOf = new BoyerMoore(parent).indexOf(str);
        System.out.println(indexOf);
    }
}
