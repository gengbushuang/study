package com.pattern.create.pool;

/**
 * 对象池
 * @param <T>
 */
public interface ResourcePool<T> {

    T getObject();

    T returnObject(Object object);
}
