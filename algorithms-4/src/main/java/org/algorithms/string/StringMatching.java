package org.algorithms.string;

public abstract class StringMatching {

    protected String parent;

    protected StringMatching(String parent) {
        this.parent = parent;
    }

    public abstract int indexOf(String txt);
}
