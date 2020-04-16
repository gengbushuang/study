package com.graph.HWM.bean;

import java.util.List;

public class SmallestSet {
    private String dimName;

    private List<AdCondition> conditions;


    public SmallestSet(String dimName, List<AdCondition> conditions) {
        this.dimName = dimName;
        this.conditions = conditions;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public List<AdCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<AdCondition> conditions) {
        this.conditions = conditions;
    }
}
