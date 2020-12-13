package com.threadtest.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Backoff {

    private final Logger log = LogManager.getLogger(this.getClass());

    private final int min, max;

    private int limit;

    private final Random random;

    public Backoff(int min, int max) {
        this.min = min;
        this.max = max;
        this.limit = min;

        this.random = new Random();

    }

    public void backoff() throws InterruptedException {
        int delay = random.nextInt(limit);
        limit = Math.min(max, 2 * limit);
        log.info("start backoff sleep :" + delay);
        Thread.sleep(delay);
    }

    public static void main(String[] args) {
        int n = 512;
        System.out.println((n<<1)<<1);
        System.out.println((n>>>3));
    }
}
