package org.algorithms.data.struct;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * FIFO队列
 * @param <Item>
 */
public class Queue<Item> implements Iterable<Item> {

    /**
     * 添加元素
     * @param item
     */
    public void enqueue(Item item){}

    /**
     * 删除元素
     * @return
     */
    public Item dequeue(){}

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
