package com.graph.HWM;

import com.graph.HWM.bean.*;
import com.utils.ListUtil;
import com.utils.Pair;
import org.junit.Test;

import java.util.*;

public class HWMGraphTest {

    @Test
    public void testAd() {
//        AdCondition AdCondition_11 = new AdCondition("audience_app_list",
//                ListUtil.newLists(new DimValue("11007702"), new DimValue("11029460"), new DimValue("11000367")
//                ));
//        Ad ad_1 = new Ad(20180725, 20180725, 100000, 10, 10, ListUtil.newLists(AdCondition_11));
//
//
//        AdCondition AdCondition_21 = new AdCondition("label2",
//                ListUtil.newLists(new DimValue("HE"), new DimValue("RU"), new DimValue("SA")
//                ));
//        Ad ad_2 = new Ad(20180725, 20180725, 100001, 20, 20, ListUtil.newLists(AdCondition_21));

        AdCondition AdCondition_11 = new AdCondition("device_country",ListUtil.newLists(new DimValue("India")));
        AdCondition AdCondition_12 = new AdCondition("ad_position_id",ListUtil.newLists(new DimValue("108"),new DimValue("161"),new DimValue("41")));
        Ad ad_1 = new Ad(20180725, 20180725, 100000, 10, 10, ListUtil.newLists(AdCondition_11,AdCondition_12));


        AdCondition AdCondition_21 = new AdCondition("device_country", ListUtil.newLists(new DimValue("Indonesia")));
        AdCondition AdCondition_22 = new AdCondition("ad_position_id", ListUtil.newLists(new DimValue("89"),new DimValue("161"),new DimValue("79")));
        Ad ad_2 = new Ad(20180725, 20180725, 100001, 20, 20, ListUtil.newLists(AdCondition_21,AdCondition_22));

        AdWithPid a_1 = new AdWithPid(ad_1);
        AdWithPid a_2 = new AdWithPid(ad_2);

        AdSlice adSlice = new AdSlice(ListUtil.newLists(a_1, a_2));

        List<SmallestSet> smallestSets = CreateSmallestSet(adSlice);
        List<AdCondition> smallestSetIndex = indexSmallestSet(smallestSets);
        List<String> cpStrInfo = createCartesianProduct(smallestSets);
        List<AdEdge> adEdges = createEdgeFromSupplyToDemand(adSlice, cpStrInfo, smallestSetIndex);

        showTableInsertSmallestSet(smallestSets);
        showTableInsertCartesianProduct(cpStrInfo);
        showTableInsertSupplyDemandAdjacency(adEdges, cpStrInfo);

    }


    @Test
    public void testAd2() {
        AdCondition AdCondition_11 = new AdCondition("地域", ListUtil.newLists(new DimValue("北京"), new DimValue("上海"), new DimValue("深圳")));
        Ad ad_1 = new Ad(20180725, 20180725, 100000, 10, 10, ListUtil.newLists(AdCondition_11));


        AdCondition AdCondition_21 = new AdCondition("地域", ListUtil.newLists(new DimValue("北京"), new DimValue("广州"), new DimValue("深圳")));
        AdCondition AdCondition_22 = new AdCondition("性别", ListUtil.newLists(new DimValue("女")));
        Ad ad_2 = new Ad(20180725, 20180725, 100001, 20, 20, ListUtil.newLists(AdCondition_21, AdCondition_22));

        AdCondition AdCondition_31 = new AdCondition("性别", ListUtil.newLists(new DimValue("女")));
        Ad ad_3 = new Ad(20180725, 20180725, 100002, 10, 10, ListUtil.newLists(AdCondition_31));

        AdWithPid a_1 = new AdWithPid(ad_1);
        AdWithPid a_2 = new AdWithPid(ad_2);
        AdWithPid a_3 = new AdWithPid(ad_3);

        AdSlice adSlice = new AdSlice(ListUtil.newLists(a_1, a_2, a_3));

        List<SmallestSet> smallestSets = CreateSmallestSet(adSlice);
        List<AdCondition> smallestSetIndex = indexSmallestSet(smallestSets);
        List<String> cpStrInfo = createCartesianProduct(smallestSets);
        List<AdEdge> adEdges = createEdgeFromSupplyToDemand(adSlice, cpStrInfo, smallestSetIndex);

        showTableInsertSmallestSet(smallestSets);
        showTableInsertCartesianProduct(cpStrInfo);
        showTableInsertSupplyDemandAdjacency(adEdges, cpStrInfo);

    }

    private void showTableInsertSupplyDemandAdjacency(List<AdEdge> adEdges, List<String> cpStrInfo) {
        System.out.println("---------------------------showTableInsertSupplyDemandAdjacency--------------------------------");
        for (AdEdge adEdge : adEdges) {
            for (int supplyId : adEdge.getSupplies()) {
                System.out.printf("INSERT INTO %s (begin_time, end_time, supply_id, ad_pid)VALUES (%d, %d, %d, %d)\n",
                        "tbl_supply_demand_adjacency", 20200301, 20200301, supplyId, adEdge.getAdWithPid().getAd().getAdID());
            }
        }
    }


    private void showTableInsertCartesianProduct(List<String> cpStrInfo) {
        System.out.println("---------------------------showTableInsertCartesianProduct--------------------------------");
        for (int id = 0; id < cpStrInfo.size(); id++) {
            System.out.printf("INSERT INTO %s (begin_time, end_time, info_id_set, supply_id) VALUES (%d, %d, '%s', %d)\n",
                    "tbl_cartesian_product", 20200301, 20200301, cpStrInfo.get(id), id);
        }
    }


    private void showTableInsertSmallestSet(List<SmallestSet> smallestSets) {
        System.out.println("---------------------------showTableInsertSmallestSet--------------------------------");
        for (SmallestSet sets : smallestSets) {
            String key = sets.getDimName();
            for (AdCondition condition : sets.getConditions()) {
                for(DimValue value:condition.getDimValues()){
                    System.out.printf("INSERT INTO %s (begin_time, end_time, tagname, tagvalue, info_id) VALUES (%d, %d, '%s', '%s', %d)\n",
                            "tbl_smallest_set",20200301,20200301,key,value.toString(),condition.getId());
                }
            }
        }
    }

    public List<AdEdge> createEdgeFromSupplyToDemand(AdSlice ads, List<String> cpStrInfo, List<AdCondition> smallestSetIndex) {
        List<AdEdge> adEdges = new ArrayList<>();

        List<AdWithPid> adWithPids = ads.getAdWithPids();
        for (AdWithPid adWithPid : adWithPids) {
            Ad ad = adWithPid.getAd();
            List<Integer> matchAry = createEachEdge(ad.getAdConditions(), cpStrInfo, smallestSetIndex);
            adEdges.add(new AdEdge(adWithPid, matchAry));
        }

        return adEdges;
    }

    private List<Integer> createEachEdge(List<AdCondition> adDemands, List<String> allSupply, List<AdCondition> smallestSetIndex) {

        List<Integer> result = new ArrayList<>();

        Map<String, List<DimValue>> dimMap = new HashMap<>();
        for (AdCondition adDemand : adDemands) {
            dimMap.put(adDemand.getDimName(), adDemand.getDimValues());
        }
        for (int i = 0; i < allSupply.size(); i++) {
            boolean isMatch = true;
            for (String subSupply : allSupply.get(i).split(";")) {
                int index = Integer.parseInt(subSupply);
                AdCondition condition = smallestSetIndex.get(index);
                List<DimValue> dimValues = dimMap.get(condition.getDimName());
                if (dimValues != null) {
                    if (!match(condition.getDimValues(), dimValues)) {
                        isMatch = false;
                        break;
                    }
                }
            }
            if (isMatch) {
                result.add(i);
            }
        }
        return result;
    }

    private boolean match(List<DimValue> supply, List<DimValue> demand) {
        Map<DimValue, Boolean> supplyMap = new HashMap<>();
        for (DimValue value : supply) {
            supplyMap.put(value, Boolean.TRUE);
        }

        for (DimValue dimValue : demand) {
            Boolean aBoolean = supplyMap.get(dimValue);
            if (aBoolean != null && aBoolean.booleanValue()) {
                return true;
            }
        }

        return false;
    }

    public List<String> createCartesianProduct(List<SmallestSet> smallestSets) {
        List<String> cpStrInfo = new ArrayList<>();
        int maxDepth = smallestSets.size();
        crossDim(cpStrInfo, 0, maxDepth, smallestSets, "");
        return cpStrInfo;
    }

    private void crossDim(List<String> cpStrInfo, int depth, int maxDepth, List<SmallestSet> smallestSet, String cpStr) {
        if (depth >= maxDepth) {
            cpStrInfo.add(cpStr);
            return;
        }
        for (AdCondition condition : smallestSet.get(depth).getConditions()) {
            String nowCpStr = cpStr + condition.getId();
            if (depth != maxDepth - 1) {
                nowCpStr += ";";
            }
            crossDim(cpStrInfo, depth + 1, maxDepth, smallestSet, nowCpStr);
        }
    }

    public List<AdCondition> indexSmallestSet(List<SmallestSet> dimSmallestSets) {
        int maxMapID = 0;
        List<AdCondition> smallestSetIndex = new ArrayList<>();
        for (SmallestSet smallestSet : dimSmallestSets) {
            for (AdCondition condition : smallestSet.getConditions()) {
                condition.setId(maxMapID);
                smallestSetIndex.add(condition);
                maxMapID++;
            }
        }
        return smallestSetIndex;
    }

    public List<SmallestSet> CreateSmallestSet(AdSlice adSlice) {
        List<SmallestSet> smallestSets = new ArrayList<>();

        Map<String, List<DimValue>> simpleTagSet = new HashMap<>();

        Map<String, List<List<DimValue>>> stringListMap = mergeDimValues(adSlice);
        for (Map.Entry<String, List<List<DimValue>>> entry : stringListMap.entrySet()) {
            Pair<List<AdCondition>, List<DimValue>> pair;
            if (isMultiValueDim(entry.getKey())) {
                pair = createSmallestSetForMultiValueDim(entry.getKey(), entry.getValue());
            } else {
                pair = createSmallestSetForSingleValueDim(entry.getKey(), entry.getValue());
            }

            smallestSets.add(new SmallestSet(entry.getKey(), pair.left));

            simpleTagSet.put(entry.getKey(), pair.right);
        }

        return smallestSets;
    }

    private boolean isMultiValueDim(String dimName) {
        if (dimName.equals("audience_app_list")) {
            return true;
        }
        return false;
    }

    private Pair<List<AdCondition>, List<DimValue>> createSmallestSetForMultiValueDim(String dimName, List<List<DimValue>> dimDemands) {
        List<DimValue> simpleSet = new ArrayList<>();
        List<List<DimValue>> smallestSet = new ArrayList<>();

        Map<String, Boolean> usedDimValue = new HashMap<>();

        for (List<DimValue> demand : dimDemands) {
            for (DimValue dimValue : demand) {
                Boolean ok = usedDimValue.get(dimValue.getValue());
                if (ok == null || !ok.booleanValue()) {
                    List<DimValue> eachSmallest = new ArrayList<>();
                    eachSmallest.add(dimValue);
                    eachSmallest.add(dimValue.toNot());

                    simpleSet.addAll(eachSmallest);
                    smallestSet.add(eachSmallest);
                    usedDimValue.put(dimValue.getValue(), Boolean.TRUE);
                }
            }
        }

        List<List<DimValue>> dimSupplies = new ArrayList<>();
        List<DimValue> tmpSupply = new ArrayList<>();
        crossMultiValueDim(dimSupplies, 0, smallestSet, tmpSupply);

        List<AdCondition> newDimSupplies = new ArrayList<>();
        for (List<DimValue> supply : dimSupplies) {
            newDimSupplies.add(new AdCondition(dimName, supply, 1));
        }
        return Pair.create(newDimSupplies, simpleSet);
    }

    private void crossMultiValueDim(List<List<DimValue>> supplies, int depth, List<List<DimValue>> smallestSet, List<DimValue> tmpSupply) {
        if (depth >= smallestSet.size()) {
            supplies.add(tmpSupply);
            return;
        }
        List<DimValue> dimValues = smallestSet.get(depth);
        for (DimValue value : dimValues) {
            List<DimValue> newTmpSupply = new ArrayList<>();
            newTmpSupply.addAll(tmpSupply);
            newTmpSupply.add(value);
            crossMultiValueDim(supplies, depth + 1, smallestSet, newTmpSupply);
        }
    }

    private Pair<List<AdCondition>, List<DimValue>> createSmallestSetForSingleValueDim(String dimName, List<List<DimValue>> dimDemands) {
        List<DimValue> simpleSet = new ArrayList<>();
        List<AdCondition> smallestSet = new ArrayList<>();

        Map<DimValue, Boolean> usedDimValue = new HashMap<>();
        Map<DimValue, String> dimValueToFatherId = new HashMap<>();
        Map<String, List<DimValue>> fatherIdToDimValues = new HashMap<>();

        for (int id = 0; id < dimDemands.size(); id++) {
            List<DimValue> values = dimDemands.get(id);
            for (DimValue dimValue : values) {
                dimValueToFatherId.put(dimValue, (dimValueToFatherId.get(dimValue) == null ? "" : dimValueToFatherId.get(dimValue)) + String.valueOf(id));
                if (!usedDimValue.containsKey(dimValue)) {
                    simpleSet.add(dimValue);
                    usedDimValue.put(dimValue, Boolean.TRUE);
                }
            }
        }

        for (Map.Entry<DimValue, String> entry : dimValueToFatherId.entrySet()) {
            List<DimValue> dimValues = fatherIdToDimValues.computeIfAbsent(entry.getValue(), value -> new ArrayList<>());
            dimValues.add(entry.getKey());
        }

        for (List<DimValue> dimValues : fatherIdToDimValues.values()) {
            smallestSet.add(new AdCondition(dimName, dimValues));
        }

        smallestSet.add(new AdCondition(dimName, simpleSet).toNot());
        return Pair.create(smallestSet, simpleSet);
    }

    private Map<String, List<List<DimValue>>> mergeDimValues(AdSlice adSlice) {
        Map<String, List<List<DimValue>>> conditionSet = new HashMap<>();
        List<AdWithPid> adWithPids = adSlice.getAdWithPids();
        for (AdWithPid adWithPid : adWithPids) {
            Ad ad = adWithPid.getAd();
            List<AdCondition> adConditions = ad.getAdConditions();
            for (AdCondition adCondition : adConditions) {
                String dimName = adCondition.getDimName();
                List<List<DimValue>> lists = conditionSet.computeIfAbsent(dimName, value -> new ArrayList<>());
                lists.add(adCondition.getDimValues());
            }
        }
        return conditionSet;
    }

    @Test
    public void test2() {
        Integer a = new Integer(2);

        Integer b = new Integer(2);
        System.out.println(a.equals(b));
    }
}
