package com.reactortest.multithreading;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolId = new AtomicInteger();

    private final AtomicInteger nextId = new AtomicInteger();

    private final String prefix;

    private final boolean daemon;

    private final ThreadGroup threadGroup;

    public DefaultThreadFactory(String poolName) {
        this(poolName, false);
    }

    public DefaultThreadFactory(String poolName, boolean daemon) {
        this(poolName, daemon, System.getSecurityManager() == null ? Thread.currentThread().getThreadGroup() : System.getSecurityManager().getThreadGroup());
    }

    public DefaultThreadFactory(String poolName, boolean daemon, ThreadGroup threadGroup) {
        this.prefix = poolName + '-' + poolId.incrementAndGet() + '-';
        this.daemon = daemon;
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + nextId.incrementAndGet();
        Thread t = new Thread(threadGroup, name);
        try {
            if (t.isDaemon() != daemon) {
                t.setDaemon(daemon);
            }
        } catch (Exception e) {
        }

        return t;
    }
}
