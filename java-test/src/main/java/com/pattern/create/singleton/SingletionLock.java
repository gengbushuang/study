package com.pattern.create.singleton;

/**
 * 双重检验锁机制
 */
public class SingletionLock {

    private static volatile SingletionLock instance;

    private SingletionLock() {
    }

    public static SingletionLock getInstance() {

        if (instance == null) {
            synchronized (SingletionLock.class) {
                if (instance == null) {
                    instance = new SingletionLock();
                }
            }
        }
        return instance;
    }
}
