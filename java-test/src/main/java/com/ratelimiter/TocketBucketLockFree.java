package com.ratelimiter;

import java.lang.reflect.Executable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class TocketBucketLockFree {
//    private static final Unsafe unsafe;
//    private static final long stateOffset;
//
//    static {
//        try {
//            unsafe = sun.misc.Unsafe.getUnsafe();
//            stateOffset = unsafe.objectFieldOffset
//                    (TocketBucketLockFree.class.getDeclaredField("state"));
//        } catch (Exception ex) {
//            throw new Error(ex);
//        }
//    }

    static final int SHARED_SHIFT = 22;

    static final int MAX_COUNT = (1 << SHARED_SHIFT) - 1;

    static final int TOKET_MASK = (1 << SHARED_SHIFT) - 1;

    static long sharedTime(long c) {
        return c >> SHARED_SHIFT;
    }

    static int toketCount(long c) {
        return (int) (c & TOKET_MASK);
    }

    private final int capacity;
    private final double refillTokensPerOneMillis;

//    private volatile long state;

    private AtomicLong state;

    public TocketBucketLockFree(int capacity, long refillTokens, long refillPeriodMillis) {
        this(capacity, ((double) refillTokens / (double) refillPeriodMillis));
    }

    public TocketBucketLockFree(int capacity, double permitsPerSecond) {
        //固定令牌
        this.capacity = capacity;
        //多少毫秒令牌生产
        this.refillTokensPerOneMillis = permitsPerSecond;

        this.state = new AtomicLong(((System.currentTimeMillis() << SHARED_SHIFT) | capacity));
    }

    public boolean tryAcquire(int numberTokens) {
        long timeAndTokens;
        long lastRefillTimestamp;
        int toketCount;
        do {
            timeAndTokens = state.get();
            lastRefillTimestamp = sharedTime(timeAndTokens);
            toketCount = toketCount(timeAndTokens);

//            long currentTimeMillis = System.currentTimeMillis();
//            long millisSinceLastRefill = currentTimeMillis-lastRefillTimestamp;
//            if()

            long currentTimeMillis = System.currentTimeMillis();
            if (lastRefillTimestamp < currentTimeMillis) {
                //当前请求时间减去上次请求时间
                long millisSinceLastRefill = currentTimeMillis - lastRefillTimestamp;
                //时间差乘以每秒生产令牌
                int refill = (int) (millisSinceLastRefill * refillTokensPerOneMillis);
                toketCount = Math.min(capacity, toketCount + refill);
                lastRefillTimestamp = currentTimeMillis;
            }
            if (numberTokens <= toketCount) {
                toketCount -= numberTokens;
            } else {
                return false;
            }
        } while (!updateState(timeAndTokens, ((lastRefillTimestamp << SHARED_SHIFT) | toketCount)));

        return true;
    }

    private final boolean updateState(long oldState, long newState) {
        return state.compareAndSet(oldState, newState);
//        return unsafe.compareAndSwapLong(this, stateOffset, oldState, newState);
    }


    public static void main(String[] args) throws InterruptedException {
        TocketBucketLockFree limiter = new TocketBucketLockFree(3500, 0.058d);
        int n = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(n);
        for (int j = 0; j < n; j++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; true; ) {
                        if (limiter.tryAcquire(1)) {
                            System.out.println(Thread.currentThread() + "---" + i);
                            i++;
                        }
                    }
                }
            });
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =0;true;){
//                    if(limiter.tryAcquire(1)){
//                        System.out.println(Thread.currentThread()+"---"+i);
//                        i++;
//                    }
//                }
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =0;true;){
//                    if(limiter.tryAcquire(1)){
//                        System.out.println(Thread.currentThread()+"---"+i);
//                        i++;
//
//                    }
//                }
//            }
//        }).start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(int i =0;true;){
//                    if(limiter.tryAcquire(1)){
//                        System.out.println(Thread.currentThread()+"---"+i);
//                        i++;
//
//                    }
//                }
//            }
//        }).start();

        Thread.sleep(Integer.MAX_VALUE);
    }
}
