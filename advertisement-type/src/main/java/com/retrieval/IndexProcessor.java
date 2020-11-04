package com.retrieval;

import com.retrieval.indexer.IndexManager;
import com.retrieval.model.Conjunction;
import com.retrieval.model.Disjunction;
import com.retrieval.model.Dnf;
import com.retrieval.model.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

public class IndexProcessor {

    private ConcurrentSkipListMap<Integer, IndexManager> table = new ConcurrentSkipListMap<>();

    public void add(Dnf dnf) {
        for (Disjunction disjunction : dnf.getDisjunctions()) {
            this.add(disjunction, dnf.getDocid());
        }
    }

    public void add(Disjunction disjunction, int docid) {
        Integer size = getConditionSize(disjunction);
        IndexManager indexManager = table.get(size);
        if (indexManager == null) {
            indexManager = new IndexManager(size.intValue());
            indexManager.add(disjunction, docid);
            table.put(size,indexManager);
            return;
        }
        indexManager.add(disjunction, docid);
    }


    public List<Integer> search(Query query, int top) {
        List<Integer> response = new ArrayList<>();
        for (int i = query.getSize(); i >= 0; --i) {
            if (!table.containsKey(i)) {
                continue;
            }
            List<Integer> tmp = table.get(i).search(query, top);
            response.addAll(tmp);
        }
        return response;
    }


    private Integer getConditionSize(Disjunction disjunction) {
        int dnfConjSize = 0;
        for (Conjunction conj : disjunction.getConjunctions()) {
            if (conj.isBt()) {
                dnfConjSize += 1;
            }
        }
        return Integer.valueOf(dnfConjSize);
    }
}
