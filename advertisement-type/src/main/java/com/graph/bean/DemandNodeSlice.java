package com.graph.bean;

import com.alibaba.fastjson.JSON;
import com.interfacetype.sort.AbsArray;

import java.util.Arrays;

public class DemandNodeSlice extends AbsArray<DemandNode> {

    private DemandNode[] demandNodes;

    private int size;

    public DemandNodeSlice(int count) {
        this.demandNodes = new DemandNode[count];
        this.size = 0;
    }

    public DemandNodeSlice(DemandNode... demandNodes) {
        this.demandNodes = demandNodes;
        this.size = demandNodes.length;
    }

    public void add(DemandNode demandNode) {
        if (size == demandNodes.length) {
            this.demandNodes = resize(demandNodes, size << 1);
        }
        demandNodes[size++] = demandNode;
    }


    public DemandNode get(int index) {
        return demandNodes[index];
    }


    @Override
    public int len() {
        return size;
    }

    @Override
    public void swap(int i, int j) {
        DemandNode tmp = demandNodes[i];
        demandNodes[i] = demandNodes[j];
        demandNodes[j] = tmp;
    }

    @Override
    public boolean less(int i, int j) {
        return demandNodes[i].getDemandCnt() / demandNodes[i].getMaxSupplyCnt()
                > demandNodes[j].getDemandCnt() / demandNodes[j].getMaxSupplyCnt();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(demandNodes[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
