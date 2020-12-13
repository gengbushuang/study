package com.ad.model.targeting;

public enum LogicalOpEnum {

    UNKNOWN(1), OR(2), AND(3);

    private final int code;

    LogicalOpEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
