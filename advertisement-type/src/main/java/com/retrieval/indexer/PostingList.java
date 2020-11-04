package com.retrieval.indexer;

import com.retrieval.indexer.model.DocidNode;

public class PostingList {

    private final DocidNode[] docidNodes;

    private final int size;

    private int first;

    public PostingList(DocidNode[] docidNodes) {
        this.docidNodes = docidNodes;
        this.size = docidNodes.length;
        this.first = 0;
    }


    public int advance(int docId) {
        while ((first < size) && (docidNodes[first].getDocid() < docId || !docidNodes[first].isBt())) {
            first += 1;
        }
        DocidNode docidNode = this.first();
        if (docidNode == null) {
            return -1;
        }
        return docidNode.getDocid();
    }

    public void skipToDocId(int docId) {
        while (first < size && docidNodes[first].getDocid() < docId) {
            first += 1;
        }
    }

    public DocidNode first() {
        if (first < size) {
            return docidNodes[first];
        }
        return null;
    }

    public void nextDoc() {
        first += 1;
    }
}
