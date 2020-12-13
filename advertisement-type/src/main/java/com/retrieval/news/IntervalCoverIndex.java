package com.retrieval.news;

import java.util.ArrayList;
import java.util.List;

public class IntervalCoverIndex {

    private List<PointInfo> pointInfos = new ArrayList<>();

    public void add(PointInfo pointInfo){
        pointInfos.add(pointInfo);
    }

    public List<PointInfo> getPointInfos() {
        return pointInfos;
    }
}
