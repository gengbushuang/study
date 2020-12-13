package com.retrieval.news;

public class AidIndex {

    private final long aid;
    private final int localId;

    public AidIndex(long aid,int localId){
        this.aid = aid;
        this.localId = localId;
    }

    public long getAid() {
        return aid;
    }
}
