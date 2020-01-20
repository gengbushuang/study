package org.java.thread.promise.netty;

public interface Future<V> extends java.util.concurrent.Future<V> {

    Future<V> addListener(FutureListener<? extends Future<? super V>> listener);
}
