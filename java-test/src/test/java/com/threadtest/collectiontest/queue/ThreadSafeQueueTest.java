package com.threadtest.collectiontest.queue;

public class ThreadSafeQueueTest {

    public static void main(String[] args) {
        final ThreadSafeQueue<Integer> cq = new ThreadSafeQueue(4);
        int nElems = 50;

        final Thread enqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    try {
                        cq.offer(i);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        final Thread deqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < nElems; ++i) {
                    final int elem;
                    try {
                        elem = cq.poll();
                        System.out.println(elem);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        enqThread.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        deqThread.start();

        System.out.println("Done");
    }
}
