package com.retrieval.test;

import com.retrieval.model.DocidNode;
import com.retrieval.util.PostingList;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class RangeTree {

//    private List<PointInfo> pointVec = new ArrayList<>();

    private IntervalTree intervalTree = new IntervalTree();

    private ConcurrentSkipListMap<Long, PostingList<DocidNode>> table = new ConcurrentSkipListMap<>();
    private Map<TokenRange, Boolean> rmapping = new HashMap<>();

    public void build(TokenRange range, DocidNode docidNode) {
        if (rmapping.isEmpty()) {
            PostingList<DocidNode> postingList = new PostingList<>();
            postingList.add(docidNode);
            table.put(range.getLeft(), postingList);
            table.put(range.getRight(), new PostingList<>());
            rmapping.put(range, Boolean.TRUE);
            return;
        }
        boolean isExit = true;
        for (Map.Entry<TokenRange, Boolean> entry : rmapping.entrySet()) {
            TokenRange key = entry.getKey();
            if (range.getLeft() > key.getRight()) {
                continue;
            } else if (range.getRight() < key.getLeft()) {
                continue;
            } else {
                isExit = false;
                if (key.getLeft() < range.getLeft() && key.getRight() < range.getRight()) {
                    //key[6-14] range[8-16] 6<8 && 14<16
                    PostingList<DocidNode> postingList = table.get(key.getLeft());
                    if (postingList != null) {
                        postingList = postingList.copy();
                    } else {
                        postingList = new PostingList();
                    }
                    PostingList<DocidNode> postingRange = table.get(range.getLeft());
                    if (postingRange == null) {
                        postingList.add(docidNode);
                        table.put(range.getLeft(), postingList);
                    }else{
                        postingRange.merge(postingList);
                    }
                } else if (key.getLeft() > range.getLeft() && key.getRight() > range.getRight()) {
                    //key[6-14] range[4-12] 6>4 && 14>12
                    PostingList<DocidNode> postingRange = table.get(range.getLeft());
                    if (postingRange == null) {
                        postingRange = new PostingList<>();
                        postingRange.add(docidNode);
                        table.put(range.getLeft(), postingRange);
                    }

                    PostingList<DocidNode> postingKey = table.get(key.getLeft());
                    if (postingKey != null) {
                        postingKey.add(docidNode);
                    }

                } else if (key.getLeft() < range.getLeft() && key.getRight() > range.getRight()) {
                    //key[6-14] range[8-12] 6<8 && 14>12

                } else if (key.getLeft() > range.getLeft() && key.getRight() < range.getRight()) {
                    //key[6-14] range[4-16] 6>4 && 14<16
                }
            }
        }

        if (isExit) {
            PostingList<DocidNode> postingList = new PostingList<>();
            postingList.add(docidNode);
            table.put(range.getLeft(), postingList);
            table.put(range.getRight(), new PostingList<>());
        }
        rmapping.put(range, Boolean.TRUE);
    }

    public void search2(long point) {

    }

    public void search(int point) {
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
        TokenRange r4 = new TokenRange("range", 2, 5);
        rangeTree.build(r4, new DocidNode(1, true));
//        TokenRange r5 = new TokenRange("range",15,23);
//        TokenRange r6 = new TokenRange("range",17,19);
//        TokenRange r7 = new TokenRange("range",26,26);
//        TokenRange r8 = new TokenRange("range",0,3);
        TokenRange r9 = new TokenRange("range", 9, 12);
        rangeTree.build(r9, new DocidNode(2, true));

        TokenRange r10 = new TokenRange("range", 4, 10);
        rangeTree.build(r10, new DocidNode(3, true));


//        TokenRange r10 = new TokenRange("range",19,20);
        rangeTree.search2(9l);

    }
}
