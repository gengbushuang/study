package com.retrieval.model;

import com.retrieval.indexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private int size = 0;

    private final Map<String,Token> tokenMap;

    public Query(){
        this.tokenMap = new HashMap<>();
    }

    public Map<String, Token> getTokenMap() {
        return tokenMap;
    }

    public int getSize() {
        return size;
    }
}
