package com.retrieval.indexer.section;

import java.util.Set;
import java.util.TreeSet;

public class PointInfo {
    private long point;

    private Set<Integer> ids = new TreeSet<>();

    public PointInfo(long point) {
        this.point = point;
    }


    public void add(Integer id) {
        ids.add(id);
    }

    public long getPoint() {
        return point;
    }


    public Set<Integer> getIds() {
        return ids;
    }

    public boolean delete(Integer integer) {
        return ids.remove(integer);
    }
}
