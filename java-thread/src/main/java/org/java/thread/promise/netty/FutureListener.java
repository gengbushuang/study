package org.java.thread.promise.netty;

import java.util.EventListener;

public interface FutureListener<F extends Future<?>> extends EventListener {

    void operationComplete(F future) throws Exception;
}
