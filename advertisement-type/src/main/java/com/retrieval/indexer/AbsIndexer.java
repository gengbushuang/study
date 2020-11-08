package com.retrieval.indexer;

import com.retrieval.util.PostingListIterator;

import java.util.List;

public abstract class AbsIndexer {
    private final String name;

    protected AbsIndexer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * @param value
     * @param isNot
     * @param uniqueId
     */
    protected abstract void add(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId);

    /**
     * @param value
     * @param isNot
     * @param uniqueId
     * @return
     */
    protected abstract boolean delete(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId);

    /**
     * @param value
     * @return
     */
    protected abstract List<Token> parseConjValue(com.proto.indexer.Indexer.AssignValue value);

    /**
     * @param token
     * @return
     */
    protected abstract PostingListIterator getPostingLists(Token token);
}
