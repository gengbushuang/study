package com.retrieval.indexer.basic;

import com.retrieval.indexer.Token;

import java.util.Objects;

public class TokenInt extends Token implements Comparable<TokenInt> {

    private final int val;

    public TokenInt(String name, int val) {
        super(name);
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenInt tokenInt = (TokenInt) o;
        return val == tokenInt.val;
    }

    public int getVal() {
        return val;
    }

    @Override
    public int hashCode() {
        return Objects.hash(val);
    }

    @Override
    public int compareTo(TokenInt o) {
        return this.val - o.getVal();
    }
}
