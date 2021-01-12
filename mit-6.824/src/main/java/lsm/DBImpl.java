package lsm;

import java.io.Closeable;
import java.io.IOException;
import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListMap;

public class DBImpl<K, V> implements DB<K, V>, Closeable {


    private final ConcurrentSkipListMap<K, V> map;

    public DBImpl(Comparator<K> comparator) {
        map = new ConcurrentSkipListMap(comparator);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void put(K key, V value) {

    }

    @Override
    public void delete(K key) {

    }


    public static final class Entry<K, V> {

        private final K key;
        private final V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
