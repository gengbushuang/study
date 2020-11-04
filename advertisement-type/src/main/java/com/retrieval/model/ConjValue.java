package com.retrieval.model;

public abstract class ConjValue {
    private final String type;

    public ConjValue(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
