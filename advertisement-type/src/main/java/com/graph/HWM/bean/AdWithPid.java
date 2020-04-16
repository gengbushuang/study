package com.graph.HWM.bean;

public class AdWithPid {
    private Ad ad;
    private long pid;

    public AdWithPid(Ad ad) {
        this.ad = ad;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }
}
