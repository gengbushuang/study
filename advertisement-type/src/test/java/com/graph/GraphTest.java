package com.graph;

import com.graph.bean.*;
import com.utils.SortUtil;
import org.junit.Test;


public class GraphTest {

    @Test
    public void testGraph(){

        SupplyNode supplyNode_10 = new SupplyNode(10,400,400);
        SupplyNode supplyNode_11 = new SupplyNode(11,400,400);
        SupplyNode supplyNode_12 = new SupplyNode(12,100,100);
        SupplyNode supplyNode_13 = new SupplyNode(13,100,100);
        SupplyNode supplyNode_14 = new SupplyNode(14,500,500);
        SupplyNode supplyNode_15 = new SupplyNode(15,300,300);

        DemandNode demandNode_1 = new DemandNode(20,200,new SupplyNodeslice(supplyNode_10,supplyNode_11,supplyNode_12));
        DemandNode demandNode_2 = new DemandNode(21,200,new SupplyNodeslice(supplyNode_12,supplyNode_13));
        DemandNode demandNode_3 = new DemandNode(22,1000,new SupplyNodeslice(supplyNode_10,supplyNode_11,supplyNode_12,supplyNode_13,supplyNode_14,supplyNode_15));

//        DemandNodeSlice demandNodeSlice = new DemandNodeSlice(demandNode_1,demandNode_2,demandNode_3);
//        System.out.println(demandNodeSlice);
//        for(int i = 0;i<demandNodeSlice.len();i++){
//            demandNodeSlice.get(i).totalRemain();
//        }
//        SortUtil.sort(demandNodeSlice);
//        System.out.println(demandNodeSlice);

        DemandNodeSlice demandNodeSlice = new DemandNodeSlice(demandNode_2,demandNode_1,demandNode_3);
        for(int i = 0;i<demandNodeSlice.len();i++){
            demandNodeSlice.get(i).totalRemain();
        }
        BipartiteGraph graph = new BipartiteGraph(demandNodeSlice);
        graph.filterZeroDemands();



        graph.resolveServingRate();
        System.out.println(demandNodeSlice);
    }
}
