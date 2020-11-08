package com.retrieval.util;

import java.util.concurrent.ConcurrentSkipListSet;

public class PostingList<T extends PostingListComparable<T>> implements PostingListIterable<T> {

    private ConcurrentSkipListSet<T> skipListSet = new ConcurrentSkipListSet<>();


    public void add(T t) {
        if (!skipListSet.add(t)) {
            this.update(t);
        }
    }

    public void delete(T t) {
        skipListSet.remove(t);
    }

    private void update(T t) {
        skipListSet.remove(t);
        skipListSet.add(t);
    }

    public void merge(ConcurrentSkipListSet<T> tmp){
        skipListSet.addAll(tmp);
    }

    @Override
    public String toString() {
        return "PostingList{" +
                "skipListSet=" + skipListSet +
                '}';
    }

    @Override
    public PostingListIterator<T> iterator() {
        return new PostingListArrayIterator((T[]) skipListSet.toArray(new PostingListComparable[]{}));
    }


    private class PostingListArrayIterator implements PostingListIterator<T> {

        private T[] ts;

        private int curIndex;

        private int length;

        public PostingListArrayIterator(T[] ts) {
            this.ts = ts;
            this.curIndex = 0;
            this.length = ts.length;
        }

        @Override
        public boolean hasNext() {
            return curIndex < length;
        }

        @Override
        public T next() {
            if (hasNext()) {
                return ts[curIndex++];
            }
            return null;
        }

        @Override
        public T curEntry() {
            if (hasNext()) {
                return ts[curIndex];
            }
            return null;
        }

        @Override
        public void seekTo(T t) {
            int low = curIndex;
            int high = length;
            for (; low < high; ) {
                int mid = (low + high) >> 1;
                if (ts[mid].compareLess(t) < 0) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            curIndex = low;
        }
    }

}
