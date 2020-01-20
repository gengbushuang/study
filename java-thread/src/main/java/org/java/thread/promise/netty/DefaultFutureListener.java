package org.java.thread.promise.netty;

import java.util.Arrays;

public class DefaultFutureListener {

    private FutureListener<? extends Future<?>> [] listeners;

    private int size;

    DefaultFutureListener(FutureListener<? extends Future<?>> one,FutureListener<? extends Future<?>> two){
        listeners = new FutureListener[2];
        listeners[0] = one;
        listeners[1] = two;
        size = 2;
    }

    public void add(FutureListener<? extends Future<?>> l) {
        FutureListener<? extends Future<?>>[] listeners = this.listeners;
        final int size = this.size;
        if (size == listeners.length) {
            this.listeners = listeners = Arrays.copyOf(listeners, size << 1);
        }
        listeners[size] = l;
        this.size = size + 1;
    }
}
