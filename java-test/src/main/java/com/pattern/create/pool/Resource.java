package com.pattern.create.pool;

public interface Resource<T> {

    T create();

    void close(T instance);
}
