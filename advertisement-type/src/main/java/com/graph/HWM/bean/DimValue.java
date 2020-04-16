package com.graph.HWM.bean;

import java.util.Objects;

public class DimValue {
    private String value;
    private boolean not;

    public DimValue(String value) {
        this.value = value;
    }

    public DimValue(String value, boolean not) {
        this.value = value;
        this.not = not;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DimValue dimValue = (DimValue) o;
        return not == dimValue.not &&
                Objects.equals(value, dimValue.value);
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, not);
    }

    @Override
    public String toString() {
        if (this.not) {
            return "not " + this.value;
        }
        return this.value;
    }

    public DimValue toNot() {
        return new DimValue(this.value, !this.not);
    }
}
