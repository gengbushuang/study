package com.threadtest.collectiontest.queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ThreadSafeQueue<T> extends AbstractQueue<T> {

    private final Logger log = LogManager.getLogger(this.getClass());
    //入队锁
    private Lock offerLock;
    private Condition emptyCond;
    //出队锁
    private Lock pollLock;
    private Condition fullCond;

    private Node<T> head;
    private Node<T> tail;

    final int capacity;

    AtomicInteger size;

    public ThreadSafeQueue(int cap) {

        this.offerLock = new ReentrantLock();
        this.fullCond = this.offerLock.newCondition();
        this.pollLock = new ReentrantLock();
        this.emptyCond = this.pollLock.newCondition();
        //出队
        this.head = new Node(null);
        //入队
        this.tail = head;

        this.size = new AtomicInteger(0);

        this.capacity = cap;
    }

    @Override
    public boolean offer(T item) {
        boolean consumes = false;
        offerLock.lock();
        log.info(Thread.currentThread()+" producer offer lock!");
        try {
            //full awit
            while (size.get() == capacity) {
                log.info(Thread.currentThread()+" producer offer full awit!");
                fullCond.await();
            }
            Node newNode = new Node(item);
            tail.next = newNode;
            tail = newNode;
            //累计返回原来值
            if (size.getAndIncrement() == 0) {
                consumes = true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info(Thread.currentThread()+" producer offer unlock!");
            offerLock.unlock();
        }

        //唤醒消费者消费
        if (consumes) {
            pollLock.lock();
            log.info(Thread.currentThread()+" awaken consumes poll lock!");
            try {
                emptyCond.signalAll();
            } finally {
                log.info(Thread.currentThread()+" awaken consumes poll unlock!");
                pollLock.unlock();
            }
        }
        return true;
    }

    @Override
    public T poll() {
        T result;
        boolean producers = false;
        pollLock.lock();
        log.info(Thread.currentThread()+" consume poll lock!");
        try {
            //empty awit
            while (size.get() == 0) {
                log.info(Thread.currentThread()+" consume poll empty awit!");
                emptyCond.await();
            }

            result = head.next.item;
            head = head.next;
            //累减返回原来值
            if (size.getAndDecrement() == capacity) {
                producers = true;
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info(Thread.currentThread()+" consume poll unlock!");
            pollLock.unlock();
        }

        //唤醒生产者
        if (producers) {
            offerLock.lock();
            log.info(Thread.currentThread()+" awaken producers poll lock!");
            try {
                fullCond.signalAll();
            } finally {
                log.info(Thread.currentThread()+" awaken producers poll unlock!");
                offerLock.unlock();
            }
        }

        return result;
    }

    private class Node<T> {
        T item;
        volatile Node<T> next;

        Node(T item) {
            this.item = item;
            this.next = null;
        }

        public T getItem() {
            return item;
        }
    }
}
