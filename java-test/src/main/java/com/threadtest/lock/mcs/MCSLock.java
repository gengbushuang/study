package com.threadtest.lock.mcs;

import com.threadtest.lock.Backoff;
import com.threadtest.lock.Lock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicReference;

public class MCSLock implements Lock {

    private final Logger log = LogManager.getLogger(this.getClass());
    //类似哨兵
    AtomicReference<QNode> tail;
    ThreadLocal<QNode> myNode;

    public MCSLock() {
        tail = new AtomicReference<>(null);
        myNode = new ThreadLocal<QNode>() {
            @Override
            protected QNode initialValue() {
                return new QNode();
            }
        };

    }


    @Override
    public void lock() {
        QNode qNode = myNode.get();
        QNode pred = tail.getAndSet(qNode);
        if (pred != null) {
            qNode.locked = true;
            pred.next = qNode;
            //如果前面有节点，证明有一个获取锁了，要自己进行自旋，等待上个节点释放锁
//            Backoff back = new Backoff(100, 500);
            while (qNode.locked) {
//                try {
//                    back.backoff();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void unlock() {
        QNode qNode = myNode.get();
        if (qNode.next == null) {
            if (tail.compareAndSet(qNode, null)) {
                return;
            }
            while (qNode.next == null) {
//                log.info(Thread.currentThread() + "-->unlock");
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }
        //如果当前节点下个节点不为null，证明有一个节点在获取锁，释放当前节点锁，并且脱离节点链
        qNode.next.locked = false;
        qNode.next = null;

    }

    class QNode {
        volatile boolean locked = false;
        volatile QNode next;
    }
}
