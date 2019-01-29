package org.algorithms.java.search;

/**
 * 散列拉链
 * @param <K>
 * @param <V>
 */
public class SeparateChainingHashST<K, V> {

    private int m;

    private int n;

    private SequentialSearchST<K, V>[] sts;


    public SeparateChainingHashST() {
        this(997);
    }

    public SeparateChainingHashST(int m) {
        this.m = m;
        sts = new SequentialSearchST[m];
        for (int i = 0; i < m; i++) {
            sts[i] = new SequentialSearchST<>();
        }
    }

    public int hash(K k) {
        return (k.hashCode() & 0x7FFFFFFF) % m;
    }

    public V get(K k) {
        return sts[hash(k)].get(k);
    }

    public void put(K k, V v) {
        sts[hash(k)].put(k, v);
    }
}
