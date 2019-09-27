package com.geng.json;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;

public class ObjectJsonUtil {

    public static void startObject(JsonParser par) throws IOException {
        JsonToken token = par.getCurrentToken();
        if (token == null || token == JsonToken.FIELD_NAME) {
            token = par.nextToken();
        }
        if (token == JsonToken.START_OBJECT) {
            par.nextToken();
        } else {
            throw new JsonParseException(par, "Expected start of object");
        }
    }

    public static boolean endObject(JsonParser par) {
        JsonToken token = par.getCurrentToken();
        return token != null && token != JsonToken.END_OBJECT;
    }

    public static String getCurrentName(JsonParser par) throws IOException {
        String name = par.getCurrentName();
        return name == null ? "" : name;
    }

}
