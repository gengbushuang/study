package org.java.thread.eventloop;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public abstract class ThreadEventLoop extends AbstractExecutorService {
    //没有开启
    private static final int ST_NOT_STARTED = 1;
    //进行中
    private static final int ST_STARTED = 2;
    //停止
    private static final int ST_SHUTTING_DOWN = 3;
    //关闭
    private static final int ST_SHUTDOWN = 4;
    //结束
    private static final int ST_TERMINATED = 5;
    //原子性字段的操作，可以根据反射对类里面的volatile int字段进行原子更新
    private static final AtomicIntegerFieldUpdater<ThreadEventLoop> STATE_UPDATE = AtomicIntegerFieldUpdater.newUpdater(ThreadEventLoop.class, "state");

    private final Queue<Runnable> taskQueue;

    private final int maxPendingTasks;

    private volatile int state = ST_NOT_STARTED;

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
        return state >= ST_SHUTDOWN;
    }

    @Override
    public boolean isTerminated() {
        return state == ST_TERMINATED;
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
        this.addTask(task);

        this.startThread();
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

    private void startThread() {
        if (state == ST_NOT_STARTED) {
            if (STATE_UPDATE.compareAndSet(this, ST_NOT_STARTED, ST_STARTED)) {
                try {
                    doStartThread();
                } catch (Throwable e) {
                    STATE_UPDATE.set(this, ST_NOT_STARTED);
                }

            }
        }
    }

    private void doStartThread() {
        new Thread(() -> {
            try {
                ThreadEventLoop.this.run();
            } catch (Throwable e) {

            } finally {
                for (; ; ) {
                    int oldState = this.state;
                    if (oldState >= ST_SHUTTING_DOWN || STATE_UPDATE.compareAndSet(this, oldState, ST_SHUTTING_DOWN)) {
                        break;
                    }
                }
            }
        }).start();
    }
}
