package com.lock;

import com.db.ReidsDb;

public class ReidsLockTest {

    public void testLock(){
        ReidsDb reidsDb = ReidsDb.DB();
        ReidsLock reidsLock = new ReidsLock(reidsDb);
        final String key = "key";
        final String value = "value";
        reidsLock.unlockLua(key,value);
        try {
            reidsLock.lock(key,value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reidsLock.unlockLua(key,value);
    }

    public void testThreadLock(){
        ReidsDb reidsDb = ReidsDb.DB();
        ReidsLock reidsLock = new ReidsLock(reidsDb);
        final String key = "key";
        Thread t1 = new Thread(()->{
            Thread thread = Thread.currentThread();
            String value = thread.getName();
            System.out.println(thread+"a--->开始lock");
            try {
                reidsLock.lock(key,value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread+"a--->执行任务");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread+"a--->开始unlock");
            boolean b = reidsLock.unlockLua(key, value);
            System.out.println(b);
        });
        Thread t2 = new Thread(()->{
            Thread thread = Thread.currentThread();
            String value = thread.getName();
            System.out.println(thread+"b--->开始lock");
            try {
                reidsLock.lock(key,value);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread+"b--->执行任务");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(thread+"b--->开始unlock");
            boolean b = reidsLock.unlockLua(key, value);
            System.out.println(b);
        });

        t1.start();
        t2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ReidsLockTest reidsLockTest = new ReidsLockTest();
        reidsLockTest.testThreadLock();
//        reidsLockTest.testLock();

//        ReidsDb reidsDb = ReidsDb.DB();
//        ReidsLock reidsLock = new ReidsLock(reidsDb);
//        String s = reidsDb.getJedis().get("default:lock:key");
//        System.out.println(s);
    }
}
