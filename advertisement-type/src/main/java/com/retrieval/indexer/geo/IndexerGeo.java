package com.retrieval.indexer.geo;

import com.retrieval.ValueType;
import com.retrieval.indexer.Indexer;
import com.retrieval.indexer.PostingList;
import com.retrieval.indexer.Token;
import com.retrieval.indexer.model.token.TokenGeo;
import com.retrieval.model.ConjGeoValue;
import com.retrieval.model.ConjValue;

import java.util.ArrayList;
import java.util.List;

public class IndexerGeo extends Indexer {


    IndexerGeo(String name) {
        super(name);
    }

    public void add(ConjValue value, boolean bt, int docid) {
        ValueType valueType = ValueType.ofType(value.getType());
        List<Token> tokens = parseConjValue(value, valueType);
    }

    @Override
    protected List<Token> parseConjValue(ConjValue value, ValueType valueType) {
        List<Token> tokens = new ArrayList<>(1);
        switch (valueType) {
            case GEN:
                ConjGeoValue geoValue = (ConjGeoValue) value;
                tokens.add(new TokenGeo(this.getName(), geoValue.getLon(), geoValue.getLat()));
                break;
            default:
        }
        return tokens;
    }

    @Override
    protected PostingList getPostingLists(Token token) {
        return null;
    }
}