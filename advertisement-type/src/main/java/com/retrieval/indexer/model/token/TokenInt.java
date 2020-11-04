package com.retrieval.indexer.model.token;

import com.retrieval.indexer.Token;

import java.util.Objects;

public class TokenInt extends Token implements Comparable {

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
    public int compareTo(Object o) {
        if (o instanceof TokenInt) {
            return this.val - ((TokenInt) o).getVal();
        }
        return 0;
    }
}
