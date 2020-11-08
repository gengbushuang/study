package com.retrieval.indexer.basic;

import com.retrieval.indexer.Token;

public class TokenLong extends Token implements Comparable<TokenLong> {

    private final long val;

    public TokenLong(String name, long val) {
        super(name);
        this.val = val;
    }

    public long getVal() {
        return val;
    }

    @Override
    public int compareTo(TokenLong o) {
        return (int) (this.val - o.getVal());
    }
}
