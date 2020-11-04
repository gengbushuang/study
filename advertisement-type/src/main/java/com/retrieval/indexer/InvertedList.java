package com.retrieval.indexer;

import com.retrieval.indexer.model.DocidNode;

import java.util.concurrent.ConcurrentSkipListSet;

public class InvertedList {

    private ConcurrentSkipListSet<DocidNode> skipListSet = new ConcurrentSkipListSet<>();

    public void add(int docid, boolean bt) {
//        skipListSet.add(new DocidNode(docid, bt));
        this.add(new DocidNode(docid, bt));
    }

    public void add(DocidNode docidNode){
        skipListSet.add(docidNode);
    }

    public DocidNode[] copyDataArray() {
        return skipListSet.toArray(new DocidNode[]{});
    }

}
