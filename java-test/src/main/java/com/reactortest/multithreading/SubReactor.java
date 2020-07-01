package com.reactortest.multithreading;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class SubReactor implements Runnable {

    private static final int ST_NOT_STARTED = 1;
    private static final int ST_STARTED = 2;

    private static final AtomicIntegerFieldUpdater<SubReactor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SubReactor.class, "state");

    final Selector selector;

    private volatile Thread thread;

    private final Executor executor;

    private volatile boolean running = true;

    private volatile int state = ST_NOT_STARTED;

    public SubReactor(Selector selector, Executor executor) {
        this.selector = selector;
        this.executor = executor;
    }

//    public SelectionKey registerChannl(SocketChannel channel) throws ClosedChannelException {
//        SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_READ);
//        selector.wakeup();
//        return selectionKey;
//    }

    public void registerChannl(ConnectionChannel channel) throws ClosedChannelException {
        channel.register(this);
    }

    public boolean isThread() {
        return isThread(Thread.currentThread());
    }


    public boolean isThread(Thread thread) {
        return thread == this.thread;
    }

    public Selector getSelector() {
        return selector;
    }

    @Override
    public void run() {
        while (running) {
            try {
                int select = selector.select();
                System.out.println("SubReactor:run-->" + Thread.currentThread() + ",select=" + select);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                if (selectionKeys.isEmpty()) {
                    return;
                }
                Iterator<SelectionKey> iter = selectionKeys.iterator();
                while (iter.hasNext()) {
                    final SelectionKey key = iter.next();
                    final Object object = key.attachment();
                    iter.remove();
                    if (object instanceof ConnectionChannel) {
                        processSelectedKey(key, (ConnectionChannel) object);
                    }

                }
                selectionKeys.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processSelectedKey(SelectionKey key, ConnectionChannel channel) {
        if (!key.isValid()) {

        }

        int readyOps = key.readyOps();
    }


    private void startThread() {
        if(this.state==ST_NOT_STARTED){
            if(STATE_UPDATER.compareAndSet(this,ST_NOT_STARTED,ST_STARTED)){
                doStartThread();
            }
        }
    }

    private void doStartThread() {
        assert thread == null;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                thread = Thread.currentThread();
                SubReactor.this.run();
            }
        });
    }
}
