package org.algorithms.java.string;

public class StringSearch {
    /**
     * 暴力字符串查找
     *
     * @param pat
     * @param txt
     * @return
     */
    public static int search1(String pat, String txt) {
        //要匹配的字符长度
        int m = pat.length();
        //要查找内容的长度
        int n = txt.length();
        for (int i = 0; i <= n - m; i++) {
            int j;
            //最坏情况会照成mn
            for (j = 0; j < m; j++) {
                if (txt.charAt(i + j) != pat.charAt(j)) {
                    break;
                }
            }
            if (j == m) {
                return i;
            }
        }
        return -1;
    }


    public static int search2(String pat, String txt) {
        //要匹配的字符长度
        int m = pat.length();
        //要查找内容的长度
        int n = txt.length();
        int j, i;
        //最坏情况会照成mn
        for (i = 0, j = 0; i < n && j < m; i++) {
            if (txt.charAt(i) == pat.charAt(j)) {
                j++;
            } else {
                //減去以前匹配成功的长度
                //回退到开始匹配的下标
                i = i - j;
            }
        }

        if (j == m) {
            return i - m;
        }
        return -1;
    }
}
