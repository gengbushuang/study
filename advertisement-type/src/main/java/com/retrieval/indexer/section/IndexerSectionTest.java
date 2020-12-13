package com.retrieval.indexer.section;

import com.proto.indexer.Indexer;
import com.retrieval.indexer.basic.TokenLong;

public class IndexerSectionTest {

    public static void main(String[] args) {

        IndexerSection indexerSection = new IndexerSection("rage");
        Indexer.AssignValue value1 = Indexer.AssignValue.newBuilder().addIntervals(
                Indexer.Interval.newBuilder().setEnd(6).setBegin(2).build()
        ).build();
        indexerSection.add(value1,true,1);

        Indexer.AssignValue value2 = Indexer.AssignValue.newBuilder().addIntervals(
                Indexer.Interval.newBuilder().setEnd(12).setBegin(9).build()
        ).build();
        indexerSection.add(value2,true,2);

        Indexer.AssignValue value3 = Indexer.AssignValue.newBuilder().addIntervals(
                Indexer.Interval.newBuilder().setEnd(10).setBegin(4).build()
        ).build();
        indexerSection.add(value3,true,3);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(;;) {
                    indexerSection.getPostingLists(new TokenLong("rage", 5));
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

//        indexerSection.getPostingLists(new TokenLong("rage", 6));

//        indexerSection.delete(value3,true,11);
//
//        indexerSection.add(value1,true,21);

    }
}
