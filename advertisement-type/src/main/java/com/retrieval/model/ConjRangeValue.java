package com.retrieval.model;

import com.retrieval.ValueType;

public class ConjRangeValue extends ConjValue {
    private final long begin;

    private final long end;

    public ConjRangeValue(long low, long high) {
        this(low, high, ValueType.RANGE.getType());
    }

    public ConjRangeValue(long low, long high, String type) {
        super(type);
        this.begin = low;
        this.end = high;
    }

    public long getBegin() {
        return begin;
    }

    public long getEnd() {
        return end;
    }
}
