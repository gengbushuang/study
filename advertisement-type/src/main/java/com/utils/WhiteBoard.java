package com.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WhiteBoard {

    private Map<String, Integer> stageOrder;

    private List<StageInfo> stages;


    public WhiteBoard() {
        stages = new ArrayList<>();
    }

    public void addDebugInfo(String stage, Map<String, Object> info) {

        Integer order = getStageOrder(stage);


    }


    public Integer getStageOrder(String stage) {
        Integer order = stageOrder.get(stage);
        if (order == null) {
            order = stageOrder.size();
            stageOrder.put(stage, order);
            stages.add(new StageInfo());
        }
        return order;
    }


    class StageInfo {
        Map<String, List<Map<String, Object>>> lll;
    }
}
