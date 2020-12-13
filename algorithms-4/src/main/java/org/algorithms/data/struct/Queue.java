package org.algorithms.data.struct;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * FIFO队列
 *
 * @param <Item>
 */
public class Queue<Item> implements Iterable<Item> {


    private Item[] items;

    private int head;

    private int tail;

    private int size;

    public Queue() {
        this(10);
    }

    public Queue(int num) {
        this.items = (Item[]) new Object[num];
        this.size = this.head = this.tail = 0;
    }

    /**
     * 添加元素
     *
     * @param item
     */
    public void enqueue(Item item) {
        if(size==items.length){
            resize(size<<1);
        }

        if (head == items.length) {
            head = 0;
        }
        this.items[head++] = item;
        this.size++;
    }

    /**
     * 删除元素
     *
     * @return
     */
    public Item dequeue() {
        if (tail == items.length) {
            tail = 0;
        }

        Item item = this.items[tail];
        this.items[tail] = null;
        tail++;
        this.size--;
        return item;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }


    public int size() {
        return this.size;
    }

    private void resize(int max) {
        Object[] tmp = new Object[max];
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
