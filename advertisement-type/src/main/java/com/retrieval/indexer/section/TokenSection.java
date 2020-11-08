package com.retrieval.indexer.section;

import com.retrieval.indexer.Token;

import java.util.Objects;

public class TokenSection extends Token {
    private final long left;
    private final long right;

    public TokenSection(String name, long left, long right) {
        super(name);
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
        TokenSection that = (TokenSection) o;
        return left == that.left &&
                right == that.right;
    }

    @Override
    public int hashCode() {

        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "TokenSection[" + left + "," + right + ")";
    }
}
