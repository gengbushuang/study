package com.pattern.create.factory.statics;

import java.util.HashMap;
import java.util.Map;

/**
 * 利用反newInstance方法进行简单工厂模式
 */
public class ColorInstanceRegistry {
    private Map<String, Color> registeredColor = new HashMap<>();

    public void RegisterColor(String colorId, Color color) {
        registeredColor.put(colorId, color);
    }

    public Color createColor(String type) throws IllegalAccessException, InstantiationException {
        return registeredColor.get(type).newInstance();
    }
}
