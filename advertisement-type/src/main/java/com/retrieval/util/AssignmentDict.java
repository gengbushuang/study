package com.retrieval.util;

import com.retrieval.indexer.IndexType;

import java.util.HashMap;
import java.util.Map;

public class AssignmentDict {
    private static Map<String, String> dict = new HashMap<>();

    static {
        dict.put("age", "section");
        dict.put("sex", "basic");
        dict.put("city", "basic");
    }

    public static IndexType getIndexType(String name) {
        if (!dict.containsKey(name)) {
            return IndexType.UNKNOWN;
        }
        String indexType = dict.get(name);

        IndexType type = IndexType.valueOf(indexType.toUpperCase());
        if (type != null) {
            return type;
        }
        return type;
    }
}
