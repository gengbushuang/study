package lsm;

import java.io.Closeable;

public interface DB<K, V>  {

    public void put(K key, V value);

    public void delete(K key);
}
