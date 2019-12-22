package org.algorithms.string;

/**
 * 字符串查找
 * bf算法
 * 暴力算法
 */
public class BruteForce extends StringMatching {

    protected BruteForce(String parent) {
        super(parent);
    }

    public static void main(String[] args) {
        String str1 = "ababcababa";
        String str2 = "baba";
        int indexOf = new BruteForce(str2).indexOf(str1);
        System.out.println(indexOf);
    }

    @Override
    public int indexOf(String txt) {
        char[] charTxts = txt.toCharArray();
        char[] charParents = this.parent.toCharArray();

        int len1 = charTxts.length;
        int len2 = charParents.length;

        for (int i = 0; len1 - i >= len2; ++i) {
            int j = 0;
            for (; j < len2; ++j) {
                if (charTxts[i + j] != charParents[j]) {
                    break;
                }
            }
            if (j == len2) {
                return i;
            }
        }
        return -1;
    }
}
