package com.retrieval.indexer;

import com.retrieval.ValueType;
import com.retrieval.indexer.geo.IndexerGeo;
import com.retrieval.indexer.interval.IndexerInterval;
import com.retrieval.model.ConjValue;

import java.util.List;

public abstract class Indexer {

    public static Indexer createIndexer(String name, String type) {
        if (ValueType.GEN.equals(type)) {
            return new IndexerInterval(name);
        } else {
            return new IndexerDefault(name);
        }
    }

    private final String name;

    protected Indexer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 构建文档索引
     * @param value
     * @param bt
     * @param docid
     */
    protected abstract void add(ConjValue value, boolean bt, int docid);

    protected abstract List<Token> parseConjValue(ConjValue value, ValueType valueType);

    /**
     * 查询文档集合
     * @param token
     * @return
     */
    protected abstract PostingList getPostingLists(Token token);

}
