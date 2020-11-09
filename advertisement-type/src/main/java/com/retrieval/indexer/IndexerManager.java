package com.retrieval.indexer;

import com.proto.indexer.Indexer;
import com.retrieval.model.Query;
import com.retrieval.util.ConjunctionScorer;
import com.retrieval.util.IndexerFactory;
import com.retrieval.util.PostingListIterator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IndexerManager {

    private final int dnfConjSize;

    private ConcurrentMap<String, AbsIndexer> table = new ConcurrentHashMap<>();

    public IndexerManager(int dnfConjSize) {
        this.dnfConjSize = dnfConjSize;
    }


    public void add(Indexer.Conjunction conj, long uniqueId) {
        for (Indexer.Assignment assignment : conj.getAssignmentsList()) {
            this.add(assignment, uniqueId);
        }
    }

    public void add(Indexer.Assignment assign, long uniqueId) {
        AbsIndexer indexer = table.get(assign.getName());
        if (indexer == null) {
            indexer = IndexerFactory.createIndexer(assign.getName());
            if (indexer == null) {
                return;
            }
            indexer.add(assign.getValue(), assign.getNot(), uniqueId);
            table.put(assign.getName(), indexer);
            return;
        }
        indexer.add(assign.getValue(), assign.getNot(), uniqueId);
    }

    public void delete(Indexer.Conjunction conj, long uniqueId) {
        for (Indexer.Assignment assign : conj.getAssignmentsList()) {
            if (!table.containsKey(assign.getName())) {
                continue;
            }
            table.get(assign.getName()).delete(assign.getValue(), assign.getNot(), uniqueId);
        }
    }


    private ConjunctionScorer getPostingLists(Query query) {
        ConjunctionScorer scorer = new ConjunctionScorer();
        Map<String, Token> tokenMap = query.getTokenMap();
        for (Map.Entry<String, Token> entry : tokenMap.entrySet()) {
            AbsIndexer absIndexer = table.get(entry.getKey());
            if (absIndexer == null) {
                continue;
            }
            PostingListIterator postingLists = absIndexer.getPostingLists(entry.getValue());
            scorer.addPostingList(postingLists);
        }
        return scorer;

    }

    public List<Long> search(Query query, int top) {
        ConjunctionScorer scorer = this.getPostingLists(query);
        return scorer.getDocIds(dnfConjSize);
    }
}
