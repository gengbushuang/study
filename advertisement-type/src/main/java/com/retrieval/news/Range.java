package com.retrieval.news;

import java.util.Objects;

public class Range {
    private final long left;
    private final long right;

    public Range(long left,long right){
        this.left = left;
        this.right = right;
    }

    public long getLeft() {
        return left;
    }

    public long getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range range = (Range) o;
        return left == range.left &&
                right == range.right;
    }

    @Override
    public int hashCode() {

        return Objects.hash(left, right);
    }
}
