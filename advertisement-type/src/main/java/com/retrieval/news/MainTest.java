package com.retrieval.news;

import com.proto.indexer.Indexer;

public class MainTest {

    public static void main(String[] args) {
        NewIndexProcessor indexProcessor = new NewIndexProcessor();

        Indexer.Conjunction.Builder builderConj = Indexer.Conjunction.newBuilder();

//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("id")
//                .setValue(Indexer.AssignValue.newBuilder().addIval(10000).build()));

        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("range")
                .setValue(Indexer.AssignValue.newBuilder()
                        .addIntervals( Indexer.Interval.newBuilder().setBegin(5).setEnd(8).build()).build()));


        Indexer.TargetingDNF targetingDNF1 = Indexer.TargetingDNF.newBuilder()
                .setUniqueId(4)
                .addConjunctions(builderConj.build()).build();

        indexProcessor.build(targetingDNF1,1);


        builderConj = Indexer.Conjunction.newBuilder();

//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("id")
//                .setValue(Indexer.AssignValue.newBuilder().addIval(20000).build()));

        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("range")
                .setValue(Indexer.AssignValue.newBuilder()
                        .addIntervals( Indexer.Interval.newBuilder().setBegin(6).setEnd(10).build()).build()));
        Indexer.TargetingDNF targetingDNF2 = Indexer.TargetingDNF.newBuilder()
                .setUniqueId(9)
                .addConjunctions(builderConj.build()).build();


        indexProcessor.build(targetingDNF2,2);


//        builderConj = Indexer.Conjunction.newBuilder();
//
//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("id")
//                .setValue(Indexer.AssignValue.newBuilder().addIval(30000).build()));
//
//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("range")
//                .setValue(Indexer.AssignValue.newBuilder()
//                        .addIntervals( Indexer.Interval.newBuilder().setBegin(200).setEnd(300).build()).build()));
//        Indexer.TargetingDNF targetingDNF3 = Indexer.TargetingDNF.newBuilder()
//                .setUniqueId(400)
//                .addConjunctions(builderConj.build()).build();
//
//        indexProcessor.build(targetingDNF3,3);
//
//
//        builderConj = Indexer.Conjunction.newBuilder();
//
//
//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("range")
//                .setValue(Indexer.AssignValue.newBuilder()
//                        .addIntervals( Indexer.Interval.newBuilder().setBegin(200).setEnd(300).build()).build()));
//        builderConj.addAssignments(Indexer.Assignment.newBuilder().setName("id")
//                .setValue(Indexer.AssignValue.newBuilder().addIval(30000).build()));
//        Indexer.TargetingDNF targetingDNF4 = Indexer.TargetingDNF.newBuilder()
//                .setUniqueId(500)
//                .addConjunctions(builderConj.build()).build();
//
//        indexProcessor.build(targetingDNF3,4);
    }
}
