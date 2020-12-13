package com.threadtest.lock;

import com.threadtest.lock.mcs.MCSLock;

public class MCSLockTest {
    private static int j = 0;
    public static void main(String[] args) throws InterruptedException {
        Lock lock = new MCSLock();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 1000;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->startA"+j);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 1000;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->startB"+j);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 1000;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->startC"+j);
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();
    }
}
