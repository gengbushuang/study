package com.collectiontest.lru;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简易的LRU
 *
 * @param <K>
 * @param <V>
 */
public class LinkedHashMapLRUCache<K, V> {

    private final LinkedHashMap<K, V> cache;

    public LinkedHashMapLRUCache(int capacity) {
        this(capacity, false);
    }

    public LinkedHashMapLRUCache(int capacity, boolean sync) {
        if (sync) {
            cache = new SyncLRUMap(capacity);
        } else {
            cache = new LRUMap(capacity);
        }
    }

    public V get(K k) {
        return cache.get(k);
    }

    public V load(K k, V v) {
        return cache.put(k, v);
    }

    public V remove(K k) {
        return cache.remove(k);
    }

    public void clear() {
        cache.clear();
    }

    static class LRUMap<K, V> extends LinkedHashMap<K, V> {

        private final int capacity;

        LRUMap(int capacity) {
            //accessOrder为true访问的顺序 为false插入顺序
            // 就是遍历的时候显示不同
            super(capacity, 0.75F, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return this.size() > capacity;
        }
    }

    static class SyncLRUMap<K, V> extends LRUMap<K, V> {
        private ReadWriteLock rw = new ReentrantReadWriteLock();

        SyncLRUMap(int capacity) {
            super(capacity);
        }

        @Override
        public V get(Object key) {
            rw.readLock().lock();
            try {
                return super.get(key);
            } finally {
                rw.readLock().unlock();
            }

        }

        @Override
        public void clear() {
            rw.writeLock().lock();
            try {
                super.clear();
            } finally {
                rw.writeLock().unlock();
            }
        }

        @Override
        public V put(K key, V value) {
            rw.writeLock().lock();
            try {
                return super.put(key, value);
            } finally {
                rw.writeLock().unlock();
            }
        }

        @Override
        public V remove(Object key) {
            rw.writeLock().lock();
            try {
                return super.remove(key);
            } finally {
                rw.writeLock().unlock();
            }
        }
    }

    public static void main(String[] args) {

    }
}
