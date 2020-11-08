package com.retrieval.util;

import java.util.Iterator;
import java.util.List;

public interface PostingListIterator<T> extends Iterator<T> {

    T curEntry();

    void seekTo(T t);
}
