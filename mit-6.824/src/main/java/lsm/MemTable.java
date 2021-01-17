package lsm;

import java.util.Comparator;
import java.util.concurrent.ConcurrentSkipListMap;

public class MemTable<K, V> {

    private final ConcurrentSkipListMap<K, V> map;

    public MemTable(Comparator<K> comparator) {
        map = new ConcurrentSkipListMap(comparator);
    }


    public void put(K key, V val) {
        map.put(key, val);
    }
}
