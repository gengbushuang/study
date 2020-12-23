package com.threadtest.collectiontest;

import java.util.concurrent.atomic.AtomicReference;

public class lockFreeStack<Item> {
    //表示栈顶
    AtomicReference<Node<Item>> top = new AtomicReference<>();

    public void push(Item item) {
        Node newNode = new Node(item);
        Node<Item> oldNode;
        do {
            oldNode = top.get();
            newNode.next = oldNode;
        } while (!top.compareAndSet(oldNode, newNode));
    }

    public Item pop() {
        Node<Item> prevNode;
        Node<Item> nextNode;
        do {
            prevNode = top.get();
            if (prevNode == null) {
                return null;
            }
            nextNode = prevNode.next;
        } while (!top.compareAndSet(prevNode, nextNode));
        return prevNode.item;
    }

    private class Node<T> {
        final Item item;
        Node<Item> next;

        public Node(Item item) {
            this.item = item;
        }

    }
}
