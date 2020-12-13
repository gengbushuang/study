package com.threadtest.lock.clh;

import com.threadtest.lock.Lock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

public class CLHReformLock implements Lock {
    private final Logger log = LogManager.getLogger(this.getClass());

    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;

    public CLHReformLock() {
        tail = new AtomicReference<>(new QNode());
        myPred = new ThreadLocal<QNode>() {
            @Override
            protected QNode initialValue() {
                return null;
            }
        };

        myNode = new ThreadLocal<QNode>() {
            @Override
            protected QNode initialValue() {
                return new QNode();
            }
        };
    }

    /**
     *
     */
    @Override
    public void lock() {
        QNode qNode = myNode.get();
        qNode.locked = true;
        QNode pred = tail.getAndSet(qNode);
        pred.thread = Thread.currentThread();
        myPred.set(pred);
        while (pred.locked) {
            log.info("pred thread " + pred.thread + " locked " + pred.locked + " is park");
            LockSupport.park(this);
        }
    }

    @Override
    public void unlock() {
        QNode qNode = myNode.get();
        qNode.locked = false;
        log.info("pred thread " + qNode.thread + " locked " + qNode.locked + " is unpark");
        if (qNode.thread != null) {
            LockSupport.unpark(qNode.thread);
        }
        myNode.set(myPred.get());
    }


    class QNode {
        volatile boolean locked = false;
        volatile Thread thread;
    }
}
