package com.reactortest.multithreading;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class DefaultThreadExecute implements Executor {

    private final ThreadFactory factory;

    public DefaultThreadExecute(ThreadFactory threadFactory){
        this.factory = threadFactory;
    }

    @Override
    public void execute(Runnable command) {
        factory.newThread(command).start();
    }
}
