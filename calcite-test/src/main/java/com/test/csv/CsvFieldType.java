package com.test.csv;

import org.apache.calcite.adapter.java.JavaTypeFactory;
import org.apache.calcite.linq4j.tree.Primitive;
import org.apache.calcite.rel.type.RelDataType;

import java.util.HashMap;
import java.util.Map;

public enum CsvFieldType {
    STRING(String.class, "string"),
    BOOLEAN(Primitive.BOOLEAN),
    BYTE(Primitive.BYTE),
    CHAR(Primitive.CHAR),
    SHORT(Primitive.SHORT),
    INT(Primitive.INT),
    LONG(Primitive.LONG),
    FLOAT(Primitive.FLOAT),
    DOUBLE(Primitive.DOUBLE),
    DATE(java.sql.Date.class, "date"),
    TIME(java.sql.Time.class, "time"),
    TIMESTAMP(java.sql.Timestamp.class, "timestamp");


    private static final Map<String, CsvFieldType> MAP = new HashMap<>();

    static {
        for (CsvFieldType value : values()) {
            MAP.put(value.simpleName, value);
        }
    }

    private final Class clazz;
    private final String simpleName;

    CsvFieldType(Primitive primitive) {
        this(primitive.boxClass, primitive.primitiveName);
    }

    CsvFieldType(Class clazz, String simpleName) {
        this.clazz = clazz;
        this.simpleName = simpleName;
    }


    public RelDataType toType(JavaTypeFactory typeFactory) {
        RelDataType javaType = typeFactory.createJavaType(clazz);
        RelDataType sqlType = typeFactory.createSqlType(javaType.getSqlTypeName());
        return typeFactory.createTypeWithNullability(sqlType, true);
    }

    public static CsvFieldType of(String typeString) {
        return MAP.get(typeString);
    }
}
