package com.retrieval.indexer;

public enum IndexType {
    UNKNOWN("unknown"), BASIC("basic"), SECTION("section");

    String desc;

    IndexType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
