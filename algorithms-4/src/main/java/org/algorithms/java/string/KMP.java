package org.algorithms.java.string;

public class KMP {
    String pat;
    int[][] dfa;

    public KMP(String pat) {
        //构造dfa自动有限状态自动机
        this.pat = pat;
        int m = pat.length();
        int r = 256;
        dfa = new int[r][m];
        dfa[pat.charAt(0)][0] = 1;

        for (int x = 0, j = 1; j < m; j++) {
            for (int c = 0; c < r; c++) {
                dfa[c][j] = dfa[c][x];
            }
            dfa[pat.charAt(j)][j] = j + 1;
            x = dfa[pat.charAt(j)][x];
        }
    }

    public int search(String txt) {
        //要匹配的字符长度
        int m = pat.length();
        //要查找内容的长度
        int n = txt.length();
        int j, i;
        for (i = 0, j = 0; i < n && j < m; i++) {
            j = dfa[txt.charAt(i)][j];
        }
        if (j == m) {
            return i - m;
        }
        return -1;
    }

    public static void main(String[] args) {
        String pat = "AACAA";
        String txt = "AABRAACADABRAACAADABRA";
        KMP kmp = new KMP(pat);
        int index = kmp.search(txt);
        System.out.println(index);
    }
}
