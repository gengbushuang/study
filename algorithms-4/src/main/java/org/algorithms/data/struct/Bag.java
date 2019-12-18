package org.algorithms.data.struct;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 背包
 *
 * @param <Item>
 */
public class Bag<Item> implements Iterable<Item> {

    private Item[] items;

    private int size;

    public Bag() {
        this(10);
    }

    public Bag(int num) {
        this.items = (Item[]) new Object[num];
        this.size = 0;
    }

    /**
     * 添加元素
     *
     * @param item
     */
    public void add(Item item) {
        if (this.size == this.items.length) {
            resize(this.size * 2);
        }
        this.items[size++] = item;
    }


    private void resize(int max) {
        Object[] tmp = new java.lang.Object[max];
        for (int i = 0; i < size; i++) {
            tmp[i] = items[i];
        }
        this.items = (Item[]) tmp;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }


    public int size() {
        return this.size;
    }

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
