package com.pattern.create.factory.pattern;

import java.util.Map;

/**
 *
 */
public abstract class DataFactory {

    public abstract Data createData(Map<String, String> map);


    static DataFactory TABLE = new DataFactory() {
        @Override
        public Data createData(Map<String, String> map) {
            return new TableData(map);
        }
    };
}
