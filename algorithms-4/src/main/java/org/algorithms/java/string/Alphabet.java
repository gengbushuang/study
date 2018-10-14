package org.algorithms.java.string;

public class Alphabet {
    char[] chars;

    public Alphabet(String s) {
        chars = s.toCharArray();
    }

    /**
     * 获取字母表中索引位置的字符
     *
     * @param index
     * @return
     */
    public char toChar(int index) {
        return chars[index];
    }

    /**
     * 获取c的索引，在0到R-1之间
     *
     * @param c
     * @return
     */
    public int toIndex(char c) {
        for (int i = 0; i < chars.length; i++) {
            if (c == chars[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * c在字母表之中
     * @param c
     * @return
     */
//    public boolean contains(char c){
//
//    }
}
