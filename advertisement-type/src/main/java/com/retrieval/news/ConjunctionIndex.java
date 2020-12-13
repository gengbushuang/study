package com.retrieval.news;

import java.util.ArrayList;
import java.util.List;

public class ConjunctionIndex {

    private int hitCount;

    private List<Integer> uniqueIds = new ArrayList<>();

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public void add(Integer id){
        uniqueIds.add(id);
    }

    public int getHitCount() {
        return hitCount;
    }
}
