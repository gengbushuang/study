package com.threadtest.promise.js.function;

@FunctionalInterface
public interface PromiseReject<E> {

    public void reject(E e);
}
