package com.pattern.create.singleton;

/**
 * 单例模式，确保单例实例唯一性
 * 构造器私有
 * 通过声明静态方法实现全局访问
 */
public class Singletion {

    private final static Singletion instance = new Singletion();

    private Singletion() {
    }

    public static Singletion getInstance() {
        return instance;
    }
}
