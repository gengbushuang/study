package com.threadtest.collectiontest.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LockFreeQueue<Item> extends AbstractQueue<Item> {

    AtomicReference<Node<Item>> head;
    AtomicReference<Node<Item>> tail;

    public LockFreeQueue() {
        Node<Item> node = new Node<>(null);
        this.head = new AtomicReference<>(node);
        this.tail = new AtomicReference<>(node);
    }

    @Override
    public boolean offer(Item item) {

        Node<Item> newNode = new Node<>(item);
        while (true) {
            Node<Item> itemNode = tail.get();
            Node<Item> nextNode = itemNode.next.get();
            if (nextNode == null) {//为空null的话，要进行追加
                if (itemNode.next.compareAndSet(nextNode, newNode)) {//cas tail next update
                    tail.compareAndSet(itemNode, newNode);//cas tail update
                    return true;
                }
            } else {//不为空的话，cas tail update
                tail.compareAndSet(itemNode, nextNode);
            }
        }
    }

    @Override
    public Item poll() {
        Node<Item> headNode = head.get();
        Node<Item> tailNode = tail.get();
        Node<Item> nextNode = headNode.next.get();
        if(nextNode==null){

        }

        return null;
    }


    private class Node<T> {
        T item;
        AtomicReference<Node<T>> next;

        protected Node(T item) {
            this.item = item;
            this.next = new AtomicReference<>(null);
        }
    }

    public static void main(String[] args) {
        final LockFreeQueue<Integer> cq = new LockFreeQueue();
        cq.offer(1);
        cq.offer(2);
    }
}
