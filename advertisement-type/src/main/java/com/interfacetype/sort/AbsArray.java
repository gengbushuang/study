package com.interfacetype.sort;

public abstract class AbsArray<T> implements SortInterface {

    protected T[] resize(T[] ts, int max) {
        Object[] tmps = new Object[max];
        System.arraycopy(ts, 0, tmps, 0, ts.length);
        return (T[]) tmps;
    }
}
