package com.pattern.behavior.strategy.base;

import java.util.HashMap;
import java.util.Map;

public class StragegyFactory {
    static final Map<String, StartegyWarp> strategyMap = new HashMap<>();

    static {
        strategyMap.put("A", StartegyWarp.create(new AStrategy()));
        strategyMap.put("B", StartegyWarp.create(new BStrategy()));
    }

    public Strategy getStrategy(String type) {
        return getStrategy(type, Boolean.TRUE.booleanValue());
    }

    public Strategy getStrategy(String type, boolean isSingletion) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type is null");
        }
        StartegyWarp warp = strategyMap.get(type);
        if (warp != null) {
            try {
                warp.getStrategy(isSingletion);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
