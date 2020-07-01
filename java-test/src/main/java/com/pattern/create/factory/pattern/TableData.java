package com.pattern.create.factory.pattern;

import java.util.Map;

public class TableData implements Data {
    private Map<String, String> map;

    public TableData(Map<String, String> map) {
        this.map = map;
    }
}
