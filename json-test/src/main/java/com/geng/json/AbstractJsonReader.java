package com.geng.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public abstract class AbstractJsonReader {

    private final ObjectJsonFactory factory;

    protected AbstractJsonReader(ObjectJsonFactory factory){
        this.factory = factory;
    }

    public final ObjectJsonFactory factory() {
        return factory;
    }


    void readOther(Object msg, JsonParser par, String fieldName) throws IOException {

    }


    protected final boolean emptyToNull(JsonParser par) throws IOException {
        JsonToken token = par.getCurrentToken();
        if (token == null) {
            token = par.nextToken();
        }
        return !factory().isStrict() && token == null;
    }
}
