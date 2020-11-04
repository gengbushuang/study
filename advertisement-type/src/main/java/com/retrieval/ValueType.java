package com.retrieval;

public enum ValueType {
    UNKNOWN("unknown", 0), SVAL("sval", 1), IVAL("ival", 2), RANGE("range", 3), GEN("gen", 4);

    private final String type;

    private final int code;

    ValueType(String type, int code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public int getCode() {
        return code;
    }

    public static ValueType ofType(String type) {
        ValueType[] values = ValueType.values();
        for (ValueType valueType : values) {
            if (valueType.type.equals(type)) {
                return valueType;
            }
        }
        return UNKNOWN;
    }
}
