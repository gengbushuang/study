package org.algorithms.data.struct;

/**
 * 堆结构
 * root=1
 * i=数组下标 !=0
 * 左节点=i*2
 * 右节点=i*2+1
 * 父节点=i/2
 */
public class HeapStruct<T extends Comparable<T>> {

    private T[] ts;

    private int size = 0;

    public HeapStruct(int count) {
        this.ts = (T[]) new Comparable[count];
    }

    /**
     *
     * @param t
     */
    public void add(T t) {
        assert size < ts.length;
        size += 1;
        ts[size] = t;
        this.siftUp(size);

    }

    /**
     *
     * @return
     */
    public T pop() {
        assert size >= 1;
        T tmp = ts[1];

        swap(1, size);
        ts[size] = null;
        size -= 1;


        this.siftDown(1);


        return tmp;
    }


    /**
     * 自下往上，添加得时候调用
     * 在遇到根节点或者父节点小于子节点就退出循环
     *
     */
    private void siftUp(int n) {
        for (; ; ) {
            if (n == 1) {
                return;
            }
            int p = n / 2;
            //循环判断父节点得数是否小于子节点数
            if (this.less(p, n)) {
                return;
            }
            this.swap(p, n);
            n = p;
        }
    }

    /**
     * 自上往下，删除得时候调用
     * 当子节点大于父节点或者子节点超出个数就退出循环
     */
    private void siftDown(int n) {
        for (; ; ) {
            int child = n * 2;
            //判断子节点下标是否超出当前元素
            if (child > size) {
                return;
            }

            if (child + 1 <= size) {
                //如果右节点比左节点还小，就换成右节点
                if (less(child + 1, child)) {
                    child++;
                }
            }
            //判断父节点是否小于子节点
            if (less(n, child)) {
                return;
            }
            swap(n, child);
            n = child;
        }
    }

    private boolean less(int i1, int i2) {
        return ts[i1].compareTo(ts[i2]) < 0;
    }

    private void swap(int p, int i) {
        T tmp = ts[p];
        ts[p] = ts[i];
        ts[i] = tmp;
    }

    public static void main(String[] args) {
        int[] tmps = new int[]{12, 20, 15, 29, 23, 17, 22, 35, 40, 26, 51, 19};
        HeapStruct<Integer> heapStruct = new HeapStruct<>(13);
        for (int t : tmps) {
            heapStruct.add(t);
        }

        for(int i =0;i<16;i++) {
            Integer pop = heapStruct.pop();
            System.out.println(pop);
        }
    }


}
