package com.retrieval.data;

public interface Store<K,V> {

    public void add(K k,V v);

    public void delete(K k, V v);

    public boolean isExist(K k);

    public V getData(K k);
}
