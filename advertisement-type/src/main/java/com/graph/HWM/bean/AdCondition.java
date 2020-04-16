package com.graph.HWM.bean;

import java.util.ArrayList;
import java.util.List;

public class AdCondition {
    private String dimName;
    private List<DimValue> dimValues;
    private int op;
    private int id;

    public AdCondition() {

    }

    public AdCondition(String dimName, List<DimValue> dimValues) {
        this.dimName = dimName;
        this.dimValues = dimValues;
    }

    public AdCondition(String dimName, List<DimValue> dimValues, int op) {
        this(dimName, dimValues);
        this.op = op;
    }

    public AdCondition(String dimName, List<DimValue> dimValues, int op, int id) {
        this(dimName, dimValues, op);
        this.id = id;
    }

    public String getDimName() {
        return dimName;
    }

    public void setDimName(String dimName) {
        this.dimName = dimName;
    }

    public List<DimValue> getDimValues() {
        return dimValues;
    }

    public void setDimValues(List<DimValue> dimValues) {
        this.dimValues = dimValues;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AdCondition toNot() {
        List<DimValue> values = new ArrayList<>(dimValues.size());
        for (DimValue v : dimValues) {
            values.add(v.toNot());
        }
        return new AdCondition(this.dimName, values, 1 - this.op, this.id);
    }
}
