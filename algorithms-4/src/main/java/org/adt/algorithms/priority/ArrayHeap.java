package org.adt.algorithms.priority;

import org.adt.algorithms.util.Pair;


/**
 * @author gengbushuang
 * @date 2024/2/20 14:59
 */
public class ArrayHeap<T extends Comparable<T>> implements Heap<T> {

    private T[] ts;

    private int size;

    private final int divisionNum;

    public ArrayHeap(int n) {
        this(n, 2);
    }

    public ArrayHeap(int n, int d) {
        this.ts = (T[]) new Comparable[n];
        this.size = 0;
        this.divisionNum = d;
    }

    @Override
    public T top() {
        if (size <= 0) {
            return null;
        }
        T t = ts[0];
        size--;
        if (size > 0) {
            ts[0] = ts[size];
            if (size > 1) {
                pushDown(0);
            }
        }
        ts[size] = null;
        return t;
    }

    @Override
    public T peek() {
        if (size <= 0) {
            return null;
        }
        return ts[0];
    }

    @Override
    public void insert(T t) {
        //
        if (size == ts.length) {
            return;
        }
        ts[size] = t;
        size += 1;
        bubbleUpdate(size);
    }

    @Override
    public void remove(T t) {

    }

    @Override
    public void update(T oldT,T newT) {

    }


    private void pushDown(int index) {
        int currentIndex = index;
        //跟最后一个子节点的下标进行比较
        while (currentIndex < computeChildIndex(size)) {
            //找寻父节点下最高优先级的子节点
            Pair<Integer, T> pair = findHighPriorityChild(currentIndex);
            if (less(ts[currentIndex], pair.getSecond())) {
                break;
            }
            swap(currentIndex, pair.getFirst());
            currentIndex = pair.getFirst();
        }
    }

    /**
     * 找寻子节点下高优先级节点
     *
     * @param currentIndex
     * @return
     */
    private Pair<Integer, T> findHighPriorityChild(int currentIndex) {
        int index = currentIndex * divisionNum + 1;
        T max = ts[index];
        int childIndex = index;
        for (int i = 1, n = index + i; i < divisionNum && n < size; i++) {
            if (less(max, ts[n])) {
                continue;
            }
            max = ts[n];
            childIndex = n;
        }
        return Pair.create(new Integer(childIndex), max);
    }

    /**
     * 往上冒泡进行替换，子节点的权重不能大于父节点权重
     *
     * @param index
     */
    private void bubbleUpdate(int index) {
        int parentIndex = index - 1;
        while (parentIndex > 0) {
            int currentIndex = parentIndex;
            parentIndex = computeParentIndex(currentIndex);
            //判断父节点值是否大于子节点
            if (less(ts[parentIndex], ts[currentIndex])) {
                //如果父节点的值大于子节点就退出
                break;
            }
            //如果父节点的值小于子节点就进行替换
            swap(parentIndex, currentIndex);
        }

    }

    private void swap(int parentIndex, int currentIndex) {
        T tmp = ts[parentIndex];
        ts[parentIndex] = ts[currentIndex];
        ts[currentIndex] = tmp;
    }

    private boolean less(T t1, T t2) {
        return t1.compareTo(t2) > 0;
    }

    private int computeParentIndex(int index) {
        return (index - 1) / divisionNum;
    }

    /**
     * @param index
     * @return
     */
    private int computeChildIndex(int index) {
        return (index - 2) / divisionNum + 1;
    }

    public static void main(String[] args) {
        ArrayHeap<Integer> heap = new ArrayHeap(6);
        heap.insert(new Integer(1));
        heap.insert(new Integer(5));
        heap.insert(new Integer(7));
        heap.insert(new Integer(3));
        Integer top = heap.top();
        heap.top();
        heap.top();
        heap.top();
        heap.top();


//        for (int i = 0; i < 20; i++) {
////            System.out.println(i + "-->" + ((i - 2) / 2 + 1));
//            for (int k = 1; k < 4; k++) {
//                System.out.println(i + "-->" + ((i * 3) + k));
//            }
//        }

    }
}


