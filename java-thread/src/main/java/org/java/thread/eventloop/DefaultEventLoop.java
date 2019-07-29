package org.java.thread.eventloop;

public class DefaultEventLoop extends ThreadEventLoop {

    protected DefaultEventLoop(int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        super(maxPendingTasks, rejectedHandler);
    }

    @Override
    protected void run() {
        for (;;) {
            Runnable task = takeTask();
            if (task != null) {
                task.run();
            }
        }
    }
}
