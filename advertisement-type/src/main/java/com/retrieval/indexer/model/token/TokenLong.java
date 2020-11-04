package com.retrieval.indexer.model.token;

import com.retrieval.indexer.Token;

public class TokenLong extends Token {

    private final long val;

    public TokenLong(String name,long val) {
        super(name);
        this.val=val;
    }

    public long getVal() {
        return val;
    }
}
