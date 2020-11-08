package com.retrieval.indexer.section;

import java.util.ArrayList;
import java.util.List;

public class SectionCoverIndex {

    private List<PointInfo> pointInfos = new ArrayList<>();

    public void add(PointInfo pointInfo) {
        pointInfos.add(pointInfo);
    }

    public List<PointInfo> getPointInfos() {
        return pointInfos;
    }
}
