package com.threadtest.collectiontest.queue;

public class QueueFineLockTest {

    public static void main(String[] args) {
        QueueFineLock<Integer> queueFineLock = new QueueFineLock();

        queueFineLock.add(2);
//        queueFineLock.add(6);
//        queueFineLock.add(8);
//        queueFineLock.add(4);

        queueFineLock.remove(2);
    }
}
