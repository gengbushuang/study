package com.threadtest.collectiontest.queue;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 粗粒度锁队列
 */
public class QueueCrudeLock<Item extends Comparable<Item>> {

    private Node<Item> head;

    private Lock lock = new ReentrantLock();

    public QueueCrudeLock() {
        //初始化一个头信息，不用添加的时候初始化
        head = new Node<>(null, null);
    }


    public boolean add(Item item) {
        Node prev = head;
        Node curr = head.next;
        lock.lock();
        try {
            while (curr != null && curr.less(item) < 0) {
                prev = curr;
                curr = prev.next;
            }
            if (curr != null && curr.less(item) == 0) {
                return false;
            }
            Node node = new Node(item, curr);
            prev.next = node;
            return true;
        } finally {
            lock.unlock();
        }

    }

    public boolean remove(Item item) {
        Node prev = head;
        Node curr = head.next;
        lock.lock();
        try {
            while (curr != null && curr.less(item) < 0) {
                prev = curr;
                curr = prev.next;
            }

            if (curr != null && curr.less(item) == 0) {
                prev.next = curr.next;
                return true;
            }
            return false;

        } finally {
            lock.unlock();
        }
    }

    private class Node<Item extends Comparable<Item>> {
        Item item;
        Node<Item> next;

        Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;
        }

        public int less(Item o) {
            return item.compareTo(o);
        }

    }

}
