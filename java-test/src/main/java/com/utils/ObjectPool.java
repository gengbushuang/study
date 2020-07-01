package com.utils;

/**
 * 对象池
 * @param <T>
 */
public interface ObjectPool<T> {

    //获取
    T getObject();
    //返还
    void returnObject(Object object);
}
