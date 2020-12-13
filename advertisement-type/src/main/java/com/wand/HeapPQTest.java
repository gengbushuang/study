package com.wand;

public class HeapPQTest {

    public static void main(String[] args) {
        HeapPQ<Document> heapPQ = new HeapPQ<>(3);

        heapPQ.insert(new Document(1,0.5d));
        heapPQ.insert(new Document(2,1.5d));

        System.out.println(heapPQ.delMax());
        System.out.println(heapPQ.delMax());


    }
}
