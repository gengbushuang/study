package com.threadtest.collectiontest.queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 细粒度锁队列
 */
public class QueueFineLock<Item extends Comparable<Item>> extends AbstractQueue<Item> {

    private Node<Item> head;

    public QueueFineLock() {
        //
        head = new Node<>(null, null);
    }

    @Override
    public boolean add(Item item) {
        //例如数据,A,B,C,D
        //A加锁
        head.lock();
        //A变为prev
        Node prev = head;
        try {
            //而A的下个节点B变为curr
            Node curr = head.next;
            while (curr != null && curr.less(item) < 0) {
                //对B加锁
                curr.lock();
                //释放变为prev的A节点锁
                prev.unlock();
                //B从curr变为了prev
                prev = curr;
                //而B的下个节点C变为了curr
                curr = prev.next;

            }
            if (curr != null) {
                curr.lock();
            }
            try {
                if (curr != null && curr.less(item) == 0) {
                    return false;
                }

                Node node = new Node(item, curr);
                prev.next = node;
                return true;
            } finally {
                if (curr != null) {
                    curr.unlock();
                }
            }
        } finally {
            prev.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        Comparable<Item> item = (Comparable<Item>) o;
        //例如数据,A,B,C,D
        //A加锁
        head.lock();
        //A变为prev
        Node prev = head;
        try {
            //而A的下个节点B变为curr
            Node curr = head.next;
            while (curr != null && curr.less(item) < 0) {
                //对B加锁
                curr.lock();
                //释放变为prev的A节点锁
                prev.unlock();
                //B从curr变为了prev
                prev = curr;
                //而B的下个节点C变为了curr
                curr = prev.next;
            }

            if (curr != null && curr.less(item) == 0) {
                curr.lock();
                try {
                    prev.next = curr.next;
                    return true;
                } finally {
                    curr.unlock();
                }
            }
            return false;

        } finally {
            prev.unlock();
        }
    }

    private class Node<Item extends Comparable<Item>> {
        Item item;
        Node<Item> next;
        private Lock lock = new ReentrantLock();

        Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;
        }

        public int less(Item o) {
            return item.compareTo(o);
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }

    }

    public static void main(String[] args) {
        QueueFineLock<Integer> queueFineLock = new QueueFineLock();

        queueFineLock.add(2);
//        queueFineLock.add(6);
//        queueFineLock.add(8);
//        queueFineLock.add(4);

        queueFineLock.remove(2);
    }
}
