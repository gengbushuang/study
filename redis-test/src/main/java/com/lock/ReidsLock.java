package com.lock;

import com.db.ReidsDb;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * redis单节点分布式锁
 */
public class ReidsLock {

    private static final String DEL_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    private static final String DEFAULT_LOCK_PREFIX = "default:lock:";
    private static final int DEFAULT_TIMEOUT = 10000;

    private static final String LOCK_OK = "OK";

    private final ReidsDb db;

    private final String lockPrefix;

    private final int timeout;

    public ReidsLock(ReidsDb db) {
        this(db, DEFAULT_LOCK_PREFIX, DEFAULT_TIMEOUT);
    }

    public ReidsLock(ReidsDb db, String lockPrefix, int timeout) {
        this.db = db;
        this.lockPrefix = lockPrefix;
        this.timeout = timeout;
    }

    public void lock(String key, String value) throws InterruptedException {
        boolean isLock = true;
        while ((isLock = this.tryLock(key, value, timeout, TimeUnit.SECONDS)) == false) {
            Thread.sleep(500);
        }
        System.out.println(isLock);
    }

    /**
     * xx 只在键已经存在时，才对键进行设置操作
     * nx 只在键不存在时，才对键进行设置操作。SET key value NX 效果等同于 SETNX key value
     * px 设置键的过期时间为 millisecond 毫秒。SET key value PX millisecond 效果等同于 PSETEX key millisecond value
     * ex 设置键的过期时间为 second 秒。 SET key value EX second 效果等同于 SETEX key second value
     * <p>
     * 带有超时时间的分布式锁
     * <p>
     * <p>
     * <p>
     * SET key value NX EX time
     *
     * @param key
     * @param value
     * @param time
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key, String value, long time, TimeUnit unit) throws InterruptedException {
        SetParams setParams = SetParams.setParams().nx().px(unit.toMillis(time));
        Jedis jedis = db.getJedis();
        try {
            String result = jedis.set(lockPrefix + key, value, setParams);
            System.out.println(result);
            if (LOCK_OK.equals(result)) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

    /**
     * 匹配value和删除key不是一个原子操作，为了更好可以用lua方式进行删除
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        Jedis jedis = db.getJedis();
        try {
            String result = jedis.get(lockPrefix + key);
            if (value.equals(result)) {
                jedis.del(lockPrefix + key);
            }
        } finally {
            jedis.close();
        }

    }

    /**
     * 利用lua进行原子删除
     * @param key
     * @param value
     */
    public boolean unlockLua(String key, String value) {
        Jedis jedis = db.getJedis();
        try {
            Object eval = jedis.eval(DEL_LUA, Collections.singletonList(lockPrefix + key), Collections.singletonList(value));
            if (eval.toString().equals("1")) {
                return true;
            }
        } finally {
            jedis.close();
        }
        return false;
    }

}
