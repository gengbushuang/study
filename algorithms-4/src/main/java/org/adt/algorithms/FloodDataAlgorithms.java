package org.adt.algorithms;

import org.adt.algorithms.data.FloodData;
import org.adt.algorithms.util.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 总重量不超过1000KG并且热量最高食物
 *
 * @author gengbushuang
 * @date 2024/2/19 12:59
 */
public class FloodDataAlgorithms {

    /**
     * 贪心算法
     */
    public void greedy() {
        List<FloodData> floodDataList = DataUtils.initFloodDataList();

        List<FloodData> load = new ArrayList<>();
        int totalWeight = 0;

        for (FloodData floodData : floodDataList) {
            //总重量不超过1000KG
            if ((totalWeight + floodData.getWeight()) > 1000) {
                continue;
            }
            load.add(floodData);
            totalWeight += floodData.getWeight();
        }

        System.out.println(load);
    }

    public static void main(String[] args) {
        new FloodDataAlgorithms().greedy();
    }
}
