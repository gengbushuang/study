package com.retrieval.indexer.basic;

import com.google.protobuf.ProtocolStringList;
import com.retrieval.indexer.AbsIndexer;
import com.retrieval.indexer.Token;
import com.retrieval.model.DocidNode;
import com.retrieval.util.PostingList;
import com.retrieval.util.PostingListIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 基本倒排索引
 */
public class IndexerBasic extends AbsIndexer {

    private ConcurrentMap<Token, PostingList<DocidNode>> table = new ConcurrentHashMap<>();

    public IndexerBasic(String name) {
        super(name);
    }

    @Override
    protected void add(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId) {
        List<Token> tokens = parseConjValue(value);
        for (Token token : tokens) {
            this.add(token, isNot, uniqueId);
        }
    }

    @Override
    protected boolean delete(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId) {
        List<Token> tokens = parseConjValue(value);
        for (Token token : tokens) {
            if (!table.containsKey(token)) {
                continue;
            }
            table.get(token).delete(new DocidNode(uniqueId, isNot));
        }
        return false;
    }

    @Override
    protected List<Token> parseConjValue(com.proto.indexer.Indexer.AssignValue value) {
        List<Token> tokens;
        if (value.getIvalCount() > 0) {
            tokens = ConvToken(this.getName(), value.getIvalList());
        } else if (value.getSvalCount() > 0) {
            tokens = ConvToken(this.getName(), value.getSvalList());
        } else {
            tokens = Collections.EMPTY_LIST;
        }
        return tokens;
    }

    @Override
    protected PostingListIterator getPostingLists(Token token) {
        if (table.containsKey(token)) {
            PostingList<DocidNode> postingList = table.get(token);
            return postingList.iterator();
        }
        return null;
    }

    private void add(Token token, boolean isNot, long uniqueId) {
        PostingList<DocidNode> postingList = table.get(token);
        if (postingList == null) {
            postingList = new PostingList();
            postingList.add(new DocidNode(uniqueId, isNot));
            table.put(token, postingList);
            return;
        }
        postingList.add(new DocidNode(uniqueId, isNot));
    }

    private List<Token> ConvToken(String name, ProtocolStringList svalList) {
        List<Token> tokens = new ArrayList<>(svalList.size());
        for (String val : svalList) {
            tokens.add(new TokenString(name, val));
        }
        return tokens;
    }

    private List<Token> ConvToken(String name, List<Integer> ivalList) {
        List<Token> tokens = new ArrayList<>(ivalList.size());
        for (Integer val : ivalList) {
            tokens.add(new TokenInt(name, val.intValue()));
        }
        return tokens;
    }
}
