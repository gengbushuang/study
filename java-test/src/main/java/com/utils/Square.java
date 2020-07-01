package com.utils;

public class Square {

    public int sqrt(int x) {
        //这个是找的c语言上面的的公式算法
        double x1 = 1.0;
        double cheak;
        do {
            x1 = (x / x1 + x1) / 2.0;
            cheak = x1 * x1 - x;
        } while ((cheak >= 0 ? cheak : -cheak) > 0.1);

        return (int) x1;
    }

    public static void main(String[] args) {
        int x = 8;
        int sqrt = new Square().sqrt(x);
        System.out.println(sqrt);
    }
}
