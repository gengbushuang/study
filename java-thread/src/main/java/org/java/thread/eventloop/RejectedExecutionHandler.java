package org.java.thread.eventloop;

public interface RejectedExecutionHandler {

    void rejectedExecution(Runnable r, ThreadEventLoop executor);
}
