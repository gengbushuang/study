package com.pattern.create.factory.pattern;


import java.util.Map;

public class JsonData implements Data {
    private Map<String, String> map;

    public JsonData(Map<String, String> map) {
        this.map = map;
    }
}
