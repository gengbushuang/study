package com.pattern.behavior.state.base.branch;

public enum State {
    SMALL(0),SUPER(1),FIRE(2),CAPE(3)
    ;

    private int value;

    private State(int code){
        this.value = code;
    }

    public int getValue() {
        return value;
    }
}