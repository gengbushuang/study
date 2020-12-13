package com.ratelimiter.alg;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RateLimiterAlg {

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    private final int limit;

    private long lastRefillTimestamp;

    private Lock lock = new ReentrantLock();

    public RateLimiterAlg(int limit) {
        this.limit = limit;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }


    public boolean tryAcquire() {
        int count = atomicInteger.incrementAndGet();
        if (count <= limit) {
            return true;
        }
        lock.lock();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (lastRefillTimestamp < currentTimeMillis) {
                atomicInteger.set(0);
                lastRefillTimestamp = currentTimeMillis;
            }
            count = atomicInteger.incrementAndGet();
            return count <= limit;
        } finally {
            lock.unlock();
        }

    }
}
