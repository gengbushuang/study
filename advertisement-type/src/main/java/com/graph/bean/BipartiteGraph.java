package com.graph.bean;

public class BipartiteGraph {

    private DemandNodeSlice demandNodes;

    public BipartiteGraph(DemandNodeSlice demandNodeSlice) {
        this.demandNodes = demandNodeSlice;
    }

    public DemandNodeSlice getDemandNodes() {
        return demandNodes;
    }

    public void setDemandNodes(DemandNodeSlice demandNodes) {
        this.demandNodes = demandNodes;
    }


    public void filterZeroDemands() {
        DemandNodeSlice demands = new DemandNodeSlice(demandNodes.len());
        for (int i = 0; i < demandNodes.len(); i++) {
            if (demandNodes.get(i).getDemandCnt() > 0) {
                demands.add(demandNodes.get(i));
            }
        }
        this.demandNodes = demands;
    }

    public void resolveServingRate(){
        for (int i = 0; i < demandNodes.len(); i++) {
            demandNodes.get(i).resolveServingRate();
        }
    }
}
