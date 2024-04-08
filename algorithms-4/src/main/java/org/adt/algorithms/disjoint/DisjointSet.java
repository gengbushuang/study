package org.adt.algorithms.disjoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author gengbushuang
 * @date 2024/2/27 17:21
 */
public class DisjointSet<T> {

    private Map<T, Set<T>> partitionMap = new HashMap<>();

    public void init(T[] ts) {
        for (T t : ts) {
            partitionMap.put(t, createSet(t));
        }
    }

    private Set<T> createSet(T t) {
        Set<T> set = new HashSet<>();
        set.add(t);
        return set;
    }


    public boolean add(T t) {
        if (partitionMap.containsKey(t)) {
            return false;
        }
        partitionMap.put(t, createSet(t));
        return true;
    }

    public Set<T> findPartition(T x) {
        return partitionMap.get(x);
    }

    public boolean merge(T x, T y) {
        Set<T> partitionX = this.findPartition(x);
        Set<T> partitionY = this.findPartition(y);

        boolean isEq;
        Set<T> partition1;
        Set<T> partition2;
        if (partitionX.size() > partitionY.size()) {
            isEq = partitionX.containsAll(partitionY);
            partition1 = partitionX;
            partition2 = partitionY;
        } else {
            isEq = partitionY.containsAll(partitionX);
            partition2 = partitionX;
            partition1 = partitionY;
        }
        if (isEq) {
            return false;
        }
        for (T n : partition2) {
            partition1.add(n);
            partitionMap.put(n,partition1);
        }
        return true;
    }

    public boolean areDisjoint(T x, T y) {
        Set<T> partitionX = this.findPartition(x);
        Set<T> partitionY = this.findPartition(y);
        return partitionX.size() > partitionY.size() ? partitionX.containsAll(partitionY) : partitionY.containsAll(partitionX);
    }

    public static void main(String[] args) {
        DisjointSet<Integer> test = new DisjointSet<>();
        Integer [] a = {1,2,3,4,5};
        test.init(a);

        test.merge(2,3);

        test.merge(2,5);

        boolean b = test.areDisjoint(3, 5);
        System.out.println(b);

    }
}
