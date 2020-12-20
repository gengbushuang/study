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

//    private AtomicBoolean initHead = new AtomicBoolean(false);

    public QueueCrudeLock() {

    }


    public boolean add(Item item) {
        Node prev = null;
//        Node curr = head;
//        while (curr == null) {
//            if (initHead.get()) {
//                Thread.yield();
//            } else if (initHead.compareAndSet(false, true)) {
//                head = new Node<>(item, curr);
//                return true;
//            }
//        }
        lock.lock();
        Node curr = head;
        if (curr == null) {
            head = new Node<>(item, curr);
            return true;
        }
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
        Node prev = null;
        lock.lock();
        Node curr = head;
        if (curr == null) {
            return false;
        }
        try {
            while (curr != null && curr.less(item) < 0) {
                prev = curr;
                curr = prev.next;
            }

            if (curr != null && curr.less(item) == 0) {
                if (prev == null) {
                    head = curr.next;
                } else {
                    prev.next = curr.next;
                }
//                while (head==null){
//                    if(initHead.compareAndSet(true,false)){
//                        break;
//                    }
//                }
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


    public static void main(String[] args) {
        QueueCrudeLock<Integer> queueCrudeLock = new QueueCrudeLock();

        queueCrudeLock.add(2);
//        queueCrudeLock.add(6);
//        queueCrudeLock.add(8);
//        queueCrudeLock.add(4);

        queueCrudeLock.remove(2);
    }

}
