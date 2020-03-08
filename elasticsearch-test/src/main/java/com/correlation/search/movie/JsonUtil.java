package com.correlation.search.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 转换为格式化的json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    //将单个对象转换成json格式的字符串（没有格式化后的json）
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
//            log.warn("Parse object to String error", e);
            return null;
        }
    }

    //将单个对象转换成json格式的字符串（格式化后的json）
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
//            log.warn("Parse object to String error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成单个对象
    public static <T> T string2Obj(String str, Class<T> clazz) {
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成多个对象
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        try {
            return typeReference.getType().equals(String.class) ? (T) str : (T) objectMapper.readValue(str, typeReference);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }

    //将json形式的字符串数据转换成多个对象
    public static <T> T string2Obj(byte[] bytes, TypeReference<T> typeReference) {
        try {
            return (T) objectMapper.readValue(bytes, typeReference);
        } catch (IOException e) {
//            log.warn("Parse object to Object error", e);
            return null;
        }
    }
}
