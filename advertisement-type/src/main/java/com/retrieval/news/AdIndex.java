package com.retrieval.news;

import java.util.ArrayList;
import java.util.List;

public class AdIndex {
    private  long aid;
    private int adInfoId;

    private List<Integer> tokenIds = new ArrayList<>();

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public int getAdInfoId() {
        return adInfoId;
    }

    public void setAdInfoId(int adInfoId) {
        this.adInfoId = adInfoId;
    }

    public void addTokenId(Integer tokenId){
        tokenIds.add(tokenId);
    }
}
