package com.threadtest.collectiontest.stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack<Item> extends AbstractDeque<Item> {
    //表示栈顶
    //只要保证栈顶的引用正确
    AtomicReference<Node<Item>> top = new AtomicReference<>();

    @Override
    public void push(Item item) {
        Node newNode = new Node(item);
        Node<Item> oldNode;
        do {
            oldNode = top.get();
            newNode.next = oldNode;
        } while (!top.compareAndSet(oldNode, newNode));
    }

    @Override
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
