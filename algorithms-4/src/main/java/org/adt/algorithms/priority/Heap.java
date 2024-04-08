package org.adt.algorithms.priority;

/**
 * @author gengbushuang
 * @date 2024/2/20 14:17
 */
public interface Heap<Key> {

    Key top();

    Key peek();

    void insert(Key key);

    void remove(Key key);

    void update(Key oldKey,Key newKey);

}
