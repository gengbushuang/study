package com.threadtest.collectiontest.queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 细粒度锁队列
 */
public class QueueFineLock<Item extends Comparable<Item>> {


    public QueueFineLock() {

    }

    public boolean add(Item item){


    }


    private class Node<Item extends Comparable<Item>> {
        Item item;
        Node<Item> next;
        Lock lock = new ReentrantLock();

        Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;
        }

        public int less(Item o) {
            return item.compareTo(o);
        }

        public void lock(){
            lock.lock();
        }

        public void unlock(){
            lock.unlock();
        }

    }
}
