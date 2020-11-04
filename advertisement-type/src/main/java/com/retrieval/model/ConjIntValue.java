package com.retrieval.model;

import com.retrieval.ValueType;

public class ConjIntValue extends ConjValue {

    private final int[] val;

    public ConjIntValue(int[] val) {
        this(val, ValueType.IVAL.getType());
    }

    public ConjIntValue(int[] val, String type) {
        super(type);
        this.val = val;
    }

    public int[] getVal() {
        return val;
    }
}
