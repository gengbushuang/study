package com.retrieval.util;

public interface PostingListIterable<T> extends Iterable<T> {


    @Override
    PostingListIterator<T> iterator();
}
