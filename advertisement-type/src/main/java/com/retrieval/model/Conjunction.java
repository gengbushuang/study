package com.retrieval.model;

public class Conjunction {
    private final String name;

    private boolean bt;

    private final ConjValue value;

    public Conjunction(String name, ConjValue value) {
        this(name, value, Boolean.TRUE.booleanValue());
    }

    public Conjunction(String name, ConjValue value, boolean bt) {
        this.name = name;
        this.value = value;
        this.bt = bt;
    }

    public boolean isBt() {
        return bt;
    }

    public String getName() {
        return name;
    }

    public ConjValue getValue() {
        return value;
    }
}
