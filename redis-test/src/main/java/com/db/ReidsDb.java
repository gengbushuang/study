package com.db;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Function;

public class ReidsDb {

    private static ReidsDb db = new ReidsDb();

    JedisPool pool;

    private ReidsDb() {
        pool = new JedisPool(new GenericObjectPoolConfig(),"192.168.38.5", 6379,10000);
    }

    public static ReidsDb DB() {
        return db;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public <R> R callback(Function<Jedis, R> function) {
        try (Jedis jedis = pool.getResource();) {
            return function.apply(jedis);
        }
    }

    public static void main(String[] args) {
        Long callback = ReidsDb.DB().callback(x -> {
            return x.dbSize();
        });
        System.out.println(callback);
    }
}
