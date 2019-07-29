package org.java.thread.eventloop;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

public abstract class ThreadEventLoop extends AbstractExecutorService {


    private final Queue<Runnable> taskQueue;

    private final int maxPendingTasks;

    private final RejectedExecutionHandler rejectedHandler;

    protected ThreadEventLoop(int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
        this.maxPendingTasks = Math.max(16, maxPendingTasks);
        taskQueue = newTaskQueue(this.maxPendingTasks);
        this.rejectedHandler = rejectedHandler;
    }


    protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return new LinkedBlockingQueue<Runnable>(maxPendingTasks);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public List<Runnable> shutdownNow() {
        return null;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    @Override
    public boolean isTerminated() {
        return false;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
    }

    abstract protected void run();

    protected void addTask(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!offerTask(task)) {
            reject(task);
        }
    }

    final boolean offerTask(Runnable task) {

        return taskQueue.offer(task);
    }

    protected final void reject(Runnable task) {
        rejectedHandler.rejectedExecution(task, this);
    }

    protected Runnable takeTask() {
        BlockingQueue<Runnable> taskQueue = (BlockingQueue<Runnable>) this.taskQueue;
        Runnable task = null;
        try {
            task = taskQueue.take();
        } catch (InterruptedException e) {
            // Ignore
        }
        return task;
    }
}
