package com.retrieval.indexer.basic;

import com.retrieval.indexer.Token;

import java.util.Objects;

public class TokenString extends Token {

    private final String val;

    public TokenString(String name,String val) {
        super(name);
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenString that = (TokenString) o;
        return Objects.equals(val, that.val);
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }
}
