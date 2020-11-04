package com.retrieval.indexer.interval;

import com.retrieval.ValueType;
import com.retrieval.indexer.*;
import com.retrieval.indexer.model.DocidNode;
import com.retrieval.indexer.model.token.TokenLong;
import com.retrieval.indexer.model.token.TokenRange;
import com.retrieval.model.ConjRangeValue;
import com.retrieval.model.ConjValue;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class IndexerInterval extends Indexer {

    private Map<TokenRange,DocidNode> intervalMap = new HashMap();

    private ConcurrentSkipListMap<Long, InvertedList> table = new ConcurrentSkipListMap<>();

    public IndexerInterval(String name) {
        super(name);
    }

    @Override
    protected void add(ConjValue value, boolean bt, int docid) {
        ValueType valueType = ValueType.ofType(value.getType());
        List<Token> tokens = parseConjValue(value, valueType);
        for (Token token : tokens) {
            if(token instanceof TokenRange){
                TokenRange range = (TokenRange)token;
                boolean containsKey = table.containsKey(range.getLeft());
                if(!containsKey){
                    table.put(range.getLeft(),new InvertedList());
                }
                containsKey = table.containsKey(range.getRight());
                if(!containsKey){
                    table.put(range.getRight(),new InvertedList());
                }
                intervalMap.put(range,new DocidNode(docid,bt));
            }
        }

        if(!tokens.isEmpty()){
            for(Map.Entry<TokenRange, DocidNode> entry:intervalMap.entrySet()) {
                TokenRange key = entry.getKey();
                NavigableSet<Long> navigableSet = table.subMap(key.getLeft(), key.getRight()).keySet();
                for(Long l:navigableSet){
                    table.get(l).add(entry.getValue());
                }
            }
        }
    }

    @Override
    protected List<Token> parseConjValue(ConjValue value, ValueType valueType) {
        List<Token> tokens = new ArrayList<>(1);
        switch (valueType) {
            case RANGE:
                ConjRangeValue rangeValue = (ConjRangeValue) value;
                tokens.add(new TokenRange(this.getName(), rangeValue.getBegin(), rangeValue.getEnd()));
                break;
            default:
        }
        return tokens;
    }

    @Override
    protected PostingList getPostingLists(Token token) {
        if(token instanceof TokenLong){
            TokenLong tokenLong = (TokenLong)token;
            Map.Entry<Long, InvertedList> entry = table.floorEntry(tokenLong.getVal());
            DocidNode[] docidNodes = entry.getValue().copyDataArray();
            return new PostingList(docidNodes);
        }
        return null;
    }
}
