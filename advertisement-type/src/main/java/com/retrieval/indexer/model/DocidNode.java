package com.retrieval.indexer.model;

public class DocidNode implements Comparable<DocidNode> {

    private final int docid;

    private final boolean bt;

    public DocidNode(int docid, boolean bt) {
        this.docid = docid;
        this.bt = bt;
    }

    public int getDocid() {
        return docid;
    }

    public boolean isBt() {
        return bt;
    }

    @Override
    public int compareTo(DocidNode o) {
        return this.docid - o.docid;
    }
}
