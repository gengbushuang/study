package com.retrieval.news;

import java.util.ArrayList;
import java.util.List;

public class TokenIndex {
    private List<ConjunctionHit> conjunctionHits = new ArrayList<>();
    private List<AdHit> adHits = new ArrayList<>();

    public void add(ConjunctionHit conjunctionHit){
        conjunctionHits.add(conjunctionHit);
    }

    public void add(AdHit adHit){
        adHits.add(adHit);
    }

    public List<ConjunctionHit> getConjunctionHits() {
        return conjunctionHits;
    }

    public List<AdHit> getAdHits() {
        return adHits;
    }
}
