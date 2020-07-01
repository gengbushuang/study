package com.pattern.create.factory.statics;

import java.util.HashMap;
import java.util.Map;

/**
 * 利用反射机制进行工厂模式
 */
public class ColorRegistry {
    private Map<String, Class<? extends Color>> registeredColor = new HashMap<>();

    public void RegisterColor(String colorId, Class<? extends Color> colorClass) {
        registeredColor.put(colorId, colorClass);
    }

    public Color createColor(String type) throws IllegalAccessException, InstantiationException {
        Class<? extends Color> cls = registeredColor.get(type);
        return cls.newInstance();
    }
}
