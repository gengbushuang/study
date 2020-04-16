package com.graph.bean;

public class SupplyNode {

    private long id;

    private float supplyCnt;

    private float remainCnt;

    public SupplyNode(long id,float supplyCnt,float remainCnt){
        this.id = id;
        this.supplyCnt = supplyCnt;
        this.remainCnt = remainCnt;
    }

    public void updateRemainCnt(float rate) {
        this.remainCnt -= Math.min(remainCnt, supplyCnt * rate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getSupplyCnt() {
        return supplyCnt;
    }

    public void setSupplyCnt(float supplyCnt) {
        this.supplyCnt = supplyCnt;
    }

    public float getRemainCnt() {
        return remainCnt;
    }

    public void setRemainCnt(float remainCnt) {
        this.remainCnt = remainCnt;
    }

    @Override
    public String toString() {
        return "{\"id\":" + id +",\"supplyCnt\":" + supplyCnt +
                ",\"remainCnt\":" + remainCnt +'}';
    }


}
