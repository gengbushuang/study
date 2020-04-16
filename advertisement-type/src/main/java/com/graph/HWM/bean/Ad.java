package com.graph.HWM.bean;

import java.util.List;

public class Ad {
    private long beginTime;
    private long endTime;
    private long lockedID;
    private long adID;
    private long count;
    private List<AdCondition> adConditions;

    public Ad() {

    }

    public Ad(long beginTime, long endTime, long adID, long lockedID, long count, List<AdCondition> adConditions) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.lockedID = lockedID;
        this.adID = adID;
        this.count = count;
        this.adConditions = adConditions;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getLockedID() {
        return lockedID;
    }

    public void setLockedID(long lockedID) {
        this.lockedID = lockedID;
    }

    public long getAdID() {
        return adID;
    }

    public void setAdID(long adID) {
        this.adID = adID;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<AdCondition> getAdConditions() {
        return adConditions;
    }

    public void setAdConditions(List<AdCondition> adConditions) {
        this.adConditions = adConditions;
    }
}
