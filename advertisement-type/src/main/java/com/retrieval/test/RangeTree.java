package com.retrieval.test;

import com.retrieval.indexer.model.token.TokenRange;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class RangeTree {

//    private List<PointInfo> pointVec = new ArrayList<>();

    private ConcurrentSkipListMap<Long, ArrayList<Integer>> table = new ConcurrentSkipListMap<>();
    private Map<TokenRange,Integer> rmapping = new HashMap<>();

    public void build(TokenRange range,Integer id){
//        Map<Long,Boolean> existMap = new HashMap<>();
//        Set<TokenRange> tokenRanges = rmapping.keySet();
//        for(TokenRange tokenRange:tokenRanges){
//            existMap.put(tokenRange.getLeft(),Boolean.TRUE);
//            existMap.put(tokenRange.getRight(),Boolean.TRUE);
//        }

//        Set<Long> longs = existMap.keySet();
//        for(Long p:longs){
//            PointInfo pp = new PointInfo(p);
//            pointVec.add(pp);
//        }
//
//        Collections.sort(pointVec, new Comparator<PointInfo>() {
//            @Override
//            public int compare(PointInfo o1, PointInfo o2) {
//                return (int)(o1.getPoint()-o2.getPoint());
//            }
//        });
//
//        Map<Long,Integer> pMapIndex = new HashMap<>();
//
//        for(int i = 0;i<pointVec.size();i++){
//            PointInfo p = pointVec.get(i);
//            pMapIndex.put(p.getPoint(),i);
//        }
//
//        for(Map.Entry<TokenRange, Integer> entry:rmapping.entrySet()) {
//            TokenRange key = entry.getKey();
//            Integer l = pMapIndex.get(key.getLeft());
//            Integer r = pMapIndex.get(key.getRight());
//            for (int i = l.intValue(); i < r.intValue(); i++) {
//                pointVec.get(i).addId(entry.getValue());
//            }
//        }
//            for(PointInfo info :pointVec){
//                info.sort(new Comparator<Integer>() {
//                    @Override
//                    public int compare(Integer o1, Integer o2) {
//                        return o1.intValue()-o2.intValue();
//                    }
//                });
//            }
        boolean containsKey = table.containsKey(range.getLeft());
        if(!containsKey){
            table.put(range.getLeft(),new ArrayList<>());
        }

        containsKey = table.containsKey(range.getRight());
        if(!containsKey){
            table.put(range.getRight(),new ArrayList<>());
        }

            for(Map.Entry<TokenRange, Integer> entry:rmapping.entrySet()) {
                TokenRange key = entry.getKey();
                if(key.getLeft()<range.getLeft() && key.getRight()<range.getRight()){



                }else if(key.getLeft()>range.getLeft()&&key.getRight()>range.getRight()){

                }else if(key.getLeft()<range.getLeft()&&key.getRight()>range.getRight()){

                }else{
                    NavigableSet<Long> navigableSet = table.subMap(range.getLeft(), range.getRight()).keySet();
                    for(Long l:navigableSet){
                        table.get(l).add(entry.getValue());
                    }
                }
            }
    }

    public void search2(long point){
        Map.Entry<Long, ArrayList<Integer>> longArrayListEntry = table.floorEntry(point);
        System.out.println(longArrayListEntry.getValue());
    }

    public void search(int point){
//        int low = 0;
//        int high = pointVec.size();
//        for(;low<high;){
//            int mid = (low+high)>>1;
//            if(pointVec.get(mid).getPoint()<point){
//                low = mid+1;
//            }else{
//                high=mid;
//            }
//        }
//        int index = low;
//        if(pointVec.get(low).getPoint()!=point){
//            index--;
//        }
//        pointVec.get(index).showIds();
    }


    public static void main(String[] args) {
        RangeTree rangeTree = new RangeTree();
//        TokenRange r1 = new TokenRange("range",16,21);
//        TokenRange r2 = new TokenRange("range",8,9);
//        TokenRange r3 = new TokenRange("range",25,30);
        TokenRange r4 = new TokenRange("range",5,8);
        rangeTree.build(r4,4);
//        TokenRange r5 = new TokenRange("range",15,23);
//        TokenRange r6 = new TokenRange("range",17,19);
//        TokenRange r7 = new TokenRange("range",26,26);
//        TokenRange r8 = new TokenRange("range",0,3);
        TokenRange r9 = new TokenRange("range",10,12);
        rangeTree.build(r9,9);
//        TokenRange r10 = new TokenRange("range",19,20);
        rangeTree.search2(9l);

    }
}
