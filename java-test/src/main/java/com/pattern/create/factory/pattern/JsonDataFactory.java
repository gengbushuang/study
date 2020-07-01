package com.pattern.create.factory.pattern;

import java.util.Map;

public class JsonDataFactory extends DataFactory {
    @Override
    public Data createData(Map<String, String> map) {
        return new JsonData(map);
    }
}
