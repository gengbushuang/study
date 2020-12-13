package com.threadtest.lock;

import com.threadtest.lock.clh.CLHLock;

public class CLHLockTest {
    private static int j = 0;
    public static void main(String[] args) throws InterruptedException {
        Lock lock = new CLHLock();


        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 10;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->start"+j);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 10;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->start"+j);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< 10;i++) {
                    lock.lock();
                    j++;
                    System.out.println(Thread.currentThread() + "-->start"+j);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + "-->end");
                    lock.unlock();
                }
            }
        }).start();
    }
}
