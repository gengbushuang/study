package org.algorithms.data.struct;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 背包
 * @param <Item>
 */
public class Bag<Item> implements Iterable<Item> {
    /**
     * 添加元素
     * @param item
     */
    public void add(Item item){}

    public boolean isEmpty(){};

    public int size(){}

    @Override
    public Iterator<Item> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super Item> action) {

    }

    @Override
    public Spliterator<Item> spliterator() {
        return null;
    }
}
