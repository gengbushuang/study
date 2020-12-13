package com.ad.model.targeting;

public enum NodeTypeEnum {
    UNKNOWN(0), ID(1), RANGE(2);


    private final int code;

    NodeTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
