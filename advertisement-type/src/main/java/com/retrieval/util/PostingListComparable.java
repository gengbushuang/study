package com.retrieval.util;

public interface PostingListComparable<T> extends Comparable<T> {

    public int compareLess(T t);
}
