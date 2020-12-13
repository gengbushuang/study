package com.retrieval.news;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PointInfo {

    private long point;

    private List<Integer> ids = new ArrayList<>();

    public PointInfo(long point) {
        this.point = point;
    }

    public long getPoint() {
        return point;
    }

    public void add(Integer id) {
        ids.add(id);
    }

    public void sort() {
        ids.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.intValue() - o2.intValue();
            }
        });
    }
}
