package org.java.thread.promise.netty;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DefaultPromise<V> implements Promise<V> {

    private Object listeners;

    private volatile Object result;

    @Override
    public Promise<V> setSuccess(V result) {
        return null;
    }

    @Override
    public Promise<V> setFailure(Throwable cause) {
        return null;
    }

    @Override
    public Future<V> addListener(FutureListener<? extends Future<? super V>> listener) {
        synchronized (this) {
            addListener0(listener);
        }
        return this;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }


    private void addListener0(FutureListener<? extends Future<? super V>> listener) {
        if (listeners == null) {
            listeners = listener;
        } else if (listener instanceof DefaultFutureListener) {
            ((DefaultFutureListener) listeners).add(listener);
        } else {
            listeners = new DefaultFutureListener((FutureListener<?>) listeners, listener);
        }
    }
}
