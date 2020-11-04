package com.retrieval.model;

import com.retrieval.ValueType;

public class ConjStringValue extends ConjValue {
    private final String[] sval;

    public ConjStringValue(String[] val) {
        this(val, ValueType.SVAL.getType());
    }

    public ConjStringValue(String[] val, String type) {
        super(type);
        this.sval = val;
    }

    public String[] getSval() {
        return sval;
    }
}
