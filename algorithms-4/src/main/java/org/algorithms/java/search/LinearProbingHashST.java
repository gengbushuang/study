package org.algorithms.java.search;

public class LinearProbingHashST<K, V> {
    private int n;//键值对数目
    private int m = 16;//容量
    private K ks[];
    private V vs[];

    public LinearProbingHashST() {
        ks = (K[]) new Object[m];
        vs = (V[]) new Object[m];
    }

    public LinearProbingHashST(int m) {
        this.m = m;
        ks = (K[]) new Object[m];
        vs = (V[]) new Object[m];
    }

    private int hash(K k) {
        return (k.hashCode() & 0x7FFFFFFF) % m;
    }

    private void resize(int count) {
        LinearProbingHashST<K, V> lphst = new LinearProbingHashST<K, V>(count);
        for (int i = 0; i < m; i++) {
            if (ks[i] != null) {
                lphst.put(ks[i], vs[i]);
            }
        }
        this.ks = lphst.ks;
        this.vs = lphst.vs;
        this.m = lphst.m;
    }

    public void put(K k, V v) {
        if (n > m / 2) {
            resize(m * 2);
        }
        int i;
        for (i = hash(k); ks[i] != null; i = (i + 1) % m) {
            if (k.equals(ks[i])) {
                vs[i] = v;
            }
        }
        ks[i] = k;
        vs[i] = v;
        n++;
    }

    public V get(K k) {
        for (int i = hash(k); ks[i] != null; i = (i + 1) % m) {
            if (k.equals(ks[i])) {
                return vs[i];
            }
        }
        return null;
    }

    public void delete(K k) {
        //这个地方删除只能删除存在的，删除前一定要先判断存不存在
        int index = hash(k);
        while (!k.equals(ks[index])) {
            index = (index + 1) % m;
        }
        ks[index] = null;
        vs[index] = null;
        index = (index + 1) % m;

        while (ks[index] != null) {
            K k1 = ks[index];
            V v = vs[index];
            ks[index] = null;
            vs[index] = null;
            put(k1, v);
            n--;
            index = (index + 1) % m;
        }
        n--;
        if (n > 0 && n == m / 8) {
            resize(m / 2);
        }
    }
}
