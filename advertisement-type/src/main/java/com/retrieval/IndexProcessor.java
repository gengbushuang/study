package com.retrieval;

import com.proto.indexer.Indexer;
import com.retrieval.indexer.IndexerManager;
import com.retrieval.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class IndexProcessor {

    private ConcurrentSkipListMap<Integer, IndexerManager> table = new ConcurrentSkipListMap<>();

    public void add(Indexer.TargetingDNF dnf, int localId) {
        for (Indexer.Conjunction conjunction : dnf.getConjunctionsList()) {
            this.add(conjunction, dnf.getUniqueId());
        }
    }

    private void add(Indexer.Conjunction conjunction, long uniqueId) {
        Integer size = getConditionSize(conjunction);
        IndexerManager indexManager = table.get(size);
        if (indexManager == null) {
            indexManager = new IndexerManager(size.intValue());
            indexManager.add(conjunction, uniqueId);
            table.put(size, indexManager);
            return;
        }
        indexManager.add(conjunction, uniqueId);
    }

    private Integer getConditionSize(Indexer.Conjunction conjunction) {
        int dnfConjSize = 0;
        for (Indexer.Assignment assignment : conjunction.getAssignmentsList()) {
            if (assignment.getNot()) {
                dnfConjSize += 1;
            }
        }
        return Integer.valueOf(dnfConjSize);
    }

    public void update(Indexer.TargetingDNF dnf, int localId) {

    }

    public void delete(Indexer.TargetingDNF dnf, int localId) {
        for (Indexer.Conjunction conjunction : dnf.getConjunctionsList()) {
            Integer size = getConditionSize(conjunction);
            if (!table.containsKey(size)) {
                continue;
            }
            table.get(size).delete(conjunction, dnf.getUniqueId());
        }
    }


    public List<Long> search(Query query, int top) {
        List<Long> response = new ArrayList<>();
        for (int i = query.getSize(); i >= 0; --i) {
            if (!table.containsKey(i)) {
                continue;
            }
            List<Long> longs = table.get(i).search(query, top);
            response.addAll(longs);
        }
        return response;
    }


}
