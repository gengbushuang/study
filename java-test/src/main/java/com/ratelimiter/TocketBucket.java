package com.ratelimiter;

/**
 * 令牌的基本算法
 */
public class TocketBucket {

    private final long capacity;
    private final double refillTokensPerOneMillis;

    private double availableTokens;
    private long lastRefillTimestamp;


    public TocketBucket(long capacity, long refillTokens, long refillPeriodMillis) {
        this(capacity, ((double) refillTokens / (double) refillPeriodMillis));
    }

    //permitsPerSecond这个是按照毫秒的方式去计算的
    public TocketBucket(long capacity, double permitsPerSecond) {
        //固定令牌
        this.capacity = capacity;
        //多少秒令牌生产
        this.refillTokensPerOneMillis = permitsPerSecond;
        //请求令牌
        this.availableTokens = capacity;
        //当前时间
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire(int numberTokens) {
        refill();
        if (numberTokens <= availableTokens) {
            availableTokens -= numberTokens;
            return true;
        } else {
            return false;
        }

    }

    private void refill() {
        //
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > lastRefillTimestamp) {
            //当前请求时间减去上次请求时间
            long millisSinceLastRefill = currentTimeMillis - lastRefillTimestamp;
            //时间差乘以每秒生产令牌
            double refill = millisSinceLastRefill * refillTokensPerOneMillis;
            //更新令牌
            availableTokens = Math.min(capacity, availableTokens + refill);
            //更新上次请求时间
            lastRefillTimestamp = currentTimeMillis;
        }
    }

    public static void main(String[] args) throws InterruptedException {

    }
}
