package com.utils;

public class PhoneTrie {


    public void put(int phone) {

    }

    private void put(int phone, int n) {

    }


    static class Node {
        private int index;
        private Node[] nodes = new Node[11];

        public Node(int index, int phone) {

        }
    }

    public static void main(String[] args) {
        int b = 22;
        long timeMillis = System.currentTimeMillis();
        System.out.println(timeMillis);
        long n = timeMillis<<b;
//        n|=10;
        n-=1;
        System.out.println(n);
        System.out.println(n>>b);
        System.out.println(n>>>b);
        int c = (1 << b) - 1;
        System.out.println(c);
        System.out.println(n & c);
//        long p = 18610354375l;
//        long n = 10000000000l;
//        for (; true; ) {
//            System.out.println(p / n);
//            p = p % n;
//            if (p == 0) {
//                break;
//            }
//            n = n / 10;
//        }
    }
}
