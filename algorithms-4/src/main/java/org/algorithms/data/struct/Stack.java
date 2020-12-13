package org.algorithms.data.struct;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * LIFO栈
 *
 * @param <Item>
 */
public class Stack<Item> implements Iterable<Item> {


    private Item[] items;

    private int size;

    public Stack() {
        this(10);
    }

    public Stack(int num) {
        this.items = (Item[]) new Object[num];
        this.size = 0;
    }

    /**
     * 添加元素
     *
     * @param item
     */
    public void push(Item item) {
        if (this.size == items.length) {
            resize(this.size * 2);
        }
        this.items[size] = item;
        size += 1;
    }

    /**
     * 删除元素
     *
     * @return
     */
    public Item pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        if (this.size == items.length / 4) {
            resize(this.size);
        }
        size -= 1;
        Item item = this.items[size];
        this.items[size] = null;
        return item;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    private void resize(int max) {
        Object[] tmp = new java.lang.Object[max];
        for (int i = 0; i < size; i++) {
            tmp[i] = items[i];
        }
        this.items = (Item[]) tmp;
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
