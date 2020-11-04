package com.retrieval.indexer.model.token;

import com.retrieval.indexer.Token;

import java.util.Objects;

public class TokenGeo extends Token {

    private final float lon;

    private final float lat;

    public TokenGeo(String name,float lon, float lat) {
        super(name);
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenGeo tokenGeo = (TokenGeo) o;
        return Float.compare(tokenGeo.lon, lon) == 0 &&
                Float.compare(tokenGeo.lat, lat) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(lon, lat);
    }
}
