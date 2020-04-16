package com.graph.HWM.bean;

import java.util.List;

public class AdSlice {
    private List<AdWithPid> adWithPids;

    public AdSlice(List<AdWithPid> adWithPids){
        this.adWithPids = adWithPids;
    }

    public List<AdWithPid> getAdWithPids() {
        return adWithPids;
    }

    public void setAdWithPids(List<AdWithPid> adWithPids) {
        this.adWithPids = adWithPids;
    }
}
