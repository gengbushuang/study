package com.retrieval.test;

import com.retrieval.indexer.Token;

public class TokenRange extends Token {
    private final long left;
    private final long right;

    public TokenRange(String name, long left, long right) {
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
    public String toString() {
        return "TokenRange("+ left +"," + right +")";
    }
}
