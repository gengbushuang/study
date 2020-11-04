package com.retrieval.indexer;

import com.retrieval.ValueType;
import com.retrieval.indexer.model.DocidNode;
import com.retrieval.indexer.model.token.TokenInt;
import com.retrieval.indexer.model.token.TokenString;
import com.retrieval.model.ConjIntValue;
import com.retrieval.model.ConjStringValue;
import com.retrieval.model.ConjValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IndexerDefault extends Indexer {

    private ConcurrentMap<Token, InvertedList> table = new ConcurrentHashMap<>();

    IndexerDefault(String name) {
        super(name);
    }

    public void add(ConjValue value, boolean bt, int docid) {
        ValueType valueType = ValueType.ofType(value.getType());
        List<Token> tokens = parseConjValue(value, valueType);
        for (Token token : tokens) {
            this.add(token, bt, docid);
        }
    }

    private void add(Token token, boolean bt, int docid) {
        InvertedList invertedList = table.get(token);
        if (invertedList == null) {
            invertedList = new InvertedList();
            invertedList.add(docid, bt);
            table.put(token, invertedList);
            return;
        }
        invertedList.add(docid, bt);
    }

    @Override
    protected List<Token> parseConjValue(ConjValue value, ValueType valueType) {
        switch (valueType) {
            case SVAL:
                return this.ConvToken(this.getName(), ((ConjStringValue) value).getSval());
            case IVAL:
                return this.ConvToken(this.getName(), ((ConjIntValue) value).getVal());
            default:
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected PostingList getPostingLists(Token token) {
        InvertedList invertedList = table.get(token);
        if (invertedList == null) {
            return null;
        }
        DocidNode[] docidNodes = invertedList.copyDataArray();
        return new PostingList(docidNodes);
    }

    private List<Token> ConvToken(String name, String[] svals) {
        List<Token> tokens = new ArrayList<>(svals.length);
        for (String val : svals) {
            tokens.add(new TokenString(name, val));
        }
        return tokens;
    }

    private List<Token> ConvToken(String name, int[] ivals) {
        List<Token> tokens = new ArrayList<>(ivals.length);
        for (int val : ivals) {
            tokens.add(new TokenInt(name, val));
        }
        return tokens;
    }
}