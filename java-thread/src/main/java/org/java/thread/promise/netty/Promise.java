package org.java.thread.promise.netty;

public interface Promise<V> extends Future<V> {

    Promise<V> setSuccess(V result);

    Promise<V> setFailure(Throwable cause);

    @Override
    Future<V> addListener(FutureListener<? extends Future<? super V>> listener);
}
