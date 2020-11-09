package com.retrieval.model;

import com.retrieval.indexer.Token;

import java.util.Map;

public class Query {

    Map<String, Token> tokenMap;

    public Map<String, Token> getTokenMap() {
        return tokenMap;
    }

    public void addToken(String name, Token token) {
        tokenMap.put(name, token);
    }

    public int getSize() {
        return tokenMap.size();
    }
}
