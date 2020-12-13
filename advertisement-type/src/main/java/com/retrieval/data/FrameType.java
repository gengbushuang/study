package com.retrieval.data;

import org.omg.CORBA.UNKNOWN;

public enum FrameType {
    ZERO_TYPE(0),
    ALL(1),
    FIRST(2),
    MIDDLE(3),
    LAST(4),
    UNKNOWN;

    public static FrameType getFrameType(int persistentId) {
        for (FrameType frameType : FrameType.values()) {
            if (frameType != null && frameType.persistentId == persistentId) {
                return frameType;
            }
        }
        return UNKNOWN;
    }


    private final Integer persistentId;

    FrameType() {
        this.persistentId = null;
    }

    FrameType(int persistentId) {
        this.persistentId = persistentId;
    }

    public int getPersistentId() {
        return persistentId;
    }
}
