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

    //https://github.com/google/guava/issues/3381
    //初始化情况下(从4次减少到3次)，不需要初始化的情况(从2次减少到1次)
    public static SingletionLock getInstance1() {
        SingletionLock tmp = instance;
        if (tmp == null) {
            synchronized (SingletionLock.class) {
                tmp = instance;
                if (tmp == null) {
                    instance = tmp = new SingletionLock();
                }
            }
        }
        return tmp;
    }
}
