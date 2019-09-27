package com.geng.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.collect.SetMultimap;

import javax.annotation.Nullable;

public class ObjectJsonFactory {

    private JsonFactory jsonFactory;

    private boolean strict;

    private final SetMultimap<String, ObjectJsonExtReader<?>> extReaders;

    public ObjectJsonFactory(@Nullable JsonFactory jsonFactory,
                             boolean strict,
                             @Nullable SetMultimap<String, ObjectJsonExtReader<?>> extReaders) {
        this.jsonFactory = jsonFactory;
        this.strict = strict;
        this.extReaders = extReaders;
    }

    public static ObjectJsonFactory create() {
        return new ObjectJsonFactory(null, false,  null);
    }


    public final boolean isStrict() {
        return strict;
    }


    public final JsonFactory getJsonFactory() {
        if (jsonFactory == null) {
            jsonFactory = new JsonFactory();
        }
        return jsonFactory;
    }
}
