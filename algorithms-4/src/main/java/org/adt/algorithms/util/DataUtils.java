package org.adt.algorithms.util;

import org.adt.algorithms.data.FloodData;
import org.adt.algorithms.data.FunnelData;

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

    public static List<FunnelData> initFunnelDataList(){
        List<FunnelData> funnelDataList = new ArrayList<>();
        funnelDataList.add(FunnelData.create(1,"启动","2021-05-01 11:00:00"));
        funnelDataList.add(FunnelData.create(1,"首页","2021-05-01 11:10:00"));
        funnelDataList.add(FunnelData.create(1,"详情","2021-05-01 11:20:00"));
        funnelDataList.add(FunnelData.create(1,"浏览","2021-05-01 11:30:00"));
        funnelDataList.add(FunnelData.create(1,"下载","2021-05-01 11:40:00"));

        funnelDataList.add(FunnelData.create(2,"启动","2021-05-02 11:00:00"));
        funnelDataList.add(FunnelData.create(2,"首页","2021-05-02 11:10:00"));
        funnelDataList.add(FunnelData.create(2,"浏览","2021-05-02 11:20:00"));
        funnelDataList.add(FunnelData.create(2,"下载","2021-05-02 11:30:00"));

        funnelDataList.add(FunnelData.create(3,"启动","2021-05-01 11:00:00"));
        funnelDataList.add(FunnelData.create(3,"首页","2021-05-02 11:00:00"));
        funnelDataList.add(FunnelData.create(3,"详情","2021-05-03 11:00:00"));
        funnelDataList.add(FunnelData.create(3,"下载","2021-05-04 11:00:00"));

        funnelDataList.add(FunnelData.create(4,"启动","2021-05-03 11:00:00"));
        funnelDataList.add(FunnelData.create(4,"首页","2021-05-03 11:01:00"));
        funnelDataList.add(FunnelData.create(4,"首页","2021-05-03 11:02:00"));
        funnelDataList.add(FunnelData.create(4,"详情","2021-05-03 11:03:00"));
        funnelDataList.add(FunnelData.create(4,"详情","2021-05-03 11:04:00"));
        funnelDataList.add(FunnelData.create(4,"下载","2021-05-03 11:05:00"));

        return funnelDataList;
    }

    public static void main(String[] args) {
        List<FloodData> floodDataList = DataUtils.initFloodDataList();
        System.out.println(floodDataList);
    }
}
