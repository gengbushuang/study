package com.retrieval.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PointInfo {
    private final long point;

    private List<Integer> ids;

    public PointInfo(long point){
        this.point = point;
        this.ids = new ArrayList<>();
    }

    public long getPoint() {
        return point;
    }

    public void addId(Integer id){
        ids.add(id);
    }

    public void sort(Comparator<Integer> comparator){
        Collections.sort(ids,comparator);
    }

    public void showIds(){
        System.out.println(ids);
    }
}
