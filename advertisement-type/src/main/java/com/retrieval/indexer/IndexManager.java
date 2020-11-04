package com.retrieval.indexer;

import com.retrieval.model.Conjunction;
import com.retrieval.model.Disjunction;
import com.retrieval.model.Query;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IndexManager {

    private final int dnfConjSize;

    private ConcurrentMap<String, Indexer> table = new ConcurrentHashMap<>();

    public IndexManager(int dnfConjSize) {
        this.dnfConjSize = dnfConjSize;
    }


    public void add(Disjunction disjunction, int docid) {
        for (Conjunction conj : disjunction.getConjunctions()) {
            this.add(conj, docid);
        }
    }

    public void add(Conjunction conj, int docid) {
        Indexer indexer = table.get(conj.getName());
        if (indexer == null) {
            indexer = Indexer.createIndexer(conj.getName(), conj.getValue().getType());
            indexer.add(conj.getValue(), conj.isBt(), docid);
            table.put(conj.getName(), indexer);
            return;
        }
        indexer.add(conj.getValue(), conj.isBt(), docid);
    }


    private ConjunctionScorer getPostingLists(Query query) {
        ConjunctionScorer scorer = new ConjunctionScorer();
        Map<String, Token> tokenMap = query.getTokenMap();
        for (Map.Entry<String, Token> entry : tokenMap.entrySet()) {
            Indexer indexer = table.get(entry.getKey());
            if (indexer == null) {
                continue;
            }
            PostingList postingLists = indexer.getPostingLists(entry.getValue());
            scorer.addPostingList(postingLists);
        }
        return scorer;

    }

    public List<Integer> search(Query query, int top) {
        ConjunctionScorer scorer = this.getPostingLists(query);
        return scorer.getDocIds(dnfConjSize);
    }
}
