package com.pattern.behavior.state.base.lookup;

public enum Event {
    TO_SUPER(0), TO_FIRE(1), TO_CAPE(2), TO_SMALL(3);

    private int value;

    private Event(int code) {
        this.value = code;
    }

    public int getValue() {
        return value;
    }
}
