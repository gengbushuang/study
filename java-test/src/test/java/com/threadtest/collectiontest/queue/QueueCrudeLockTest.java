package com.threadtest.collectiontest.queue;

public class QueueCrudeLockTest {
    public static void main(String[] args) {
        QueueCrudeLock<Integer> queueCrudeLock = new QueueCrudeLock();

        queueCrudeLock.add(2);
//        queueCrudeLock.add(6);
//        queueCrudeLock.add(8);
//        queueCrudeLock.add(4);

        queueCrudeLock.remove(2);
    }
}
