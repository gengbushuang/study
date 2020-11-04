package com.retrieval.model;

import com.retrieval.ValueType;

public class ConjGeoValue extends ConjValue {
    private final float lon;

    private final float lat;

    public ConjGeoValue(float lon, float lat, String type) {
        super(type);
        this.lon = lon;
        this.lat = lat;
    }

    public ConjGeoValue(float lon, float lat) {
        this(lon, lat, ValueType.GEN.getType());
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
