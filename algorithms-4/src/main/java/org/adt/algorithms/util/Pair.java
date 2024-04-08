package org.adt.algorithms.util;

/**
 * @author gengbushuang
 * @date 2024/2/22 09:48
 */
public class Pair<F, S> {
    private F first;

    private S second;


    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public static <F, S> Pair<F, S> create(F first, S second) {
        Pair<F, S> pair = new Pair<>();
        pair.setFirst(first);
        pair.setSecond(second);
        return pair;
    }
}
