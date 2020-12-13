package com.retrieval.news;

public class ConjunctionHit {
    private int conjId;
    private int p;
    public ConjunctionHit(int conjId, int p) {
        this.conjId = conjId;
        this.p = p;
    }

    public int getConjId() {
        return conjId;
    }

    public void setConjId(int conjId) {
        this.conjId = conjId;
    }

    public int getP() {
        return p;
    }
}
