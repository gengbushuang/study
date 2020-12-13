package com.threadtest.lock.clh;

import com.threadtest.lock.Lock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLock implements Lock {
    private final Logger log = LogManager.getLogger(this.getClass());

    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myPred;
    ThreadLocal<QNode> myNode;

    public CLHLock() {
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
        myPred.set(pred);
        while (pred.locked) {
//            log.info(Thread.currentThread());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }

    @Override
    public void unlock() {
        QNode qNode = myNode.get();
        qNode.locked = false;
        myNode.set(myPred.get());
    }


    class QNode {
       volatile boolean  locked = false;
    }
}
