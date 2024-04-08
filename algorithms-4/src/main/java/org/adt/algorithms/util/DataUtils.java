package org.adt.algorithms.util;

import org.adt.algorithms.data.FloodData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gengbushuang
 * @date 2024/2/19 11:28
 */
public class DataUtils {

    public static List<FloodData> initFloodDataList(){
        List<FloodData> floodDataList = new ArrayList<>();

        floodDataList.add(FloodData.create("土豆",800,1501600));
        floodDataList.add(FloodData.create("面粉",400,1444000));
        floodDataList.add(FloodData.create("大米",300,1122000));
        floodDataList.add(FloodData.create("豆类",300,690000));
        floodDataList.add(FloodData.create("番茄罐头",300,237000));
        floodDataList.add(FloodData.create("草莓酱",50,130000));
        floodDataList.add(FloodData.create("花生酱",20,117800));

        return floodDataList;
    }

    public static void main(String[] args) {
        List<FloodData> floodDataList = DataUtils.initFloodDataList();
        System.out.println(floodDataList);
    }
}
