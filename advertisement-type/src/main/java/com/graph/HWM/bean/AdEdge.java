package com.graph.HWM.bean;

import java.util.List;

public class AdEdge {
    private AdWithPid adWithPid;
    private List<Integer> supplies;

    public AdEdge(AdWithPid adWithPid, List<Integer> supplies) {
        this.adWithPid = adWithPid;
        this.supplies = supplies;
    }

    public AdWithPid getAdWithPid() {
        return adWithPid;
    }

    public void setAdWithPid(AdWithPid adWithPid) {
        this.adWithPid = adWithPid;
    }

    public List<Integer> getSupplies() {
        return supplies;
    }

    public void setSupplies(List<Integer> supplies) {
        this.supplies = supplies;
    }
}
