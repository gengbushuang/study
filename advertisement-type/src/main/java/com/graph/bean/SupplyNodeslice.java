package com.graph.bean;

import com.alibaba.fastjson.JSON;
import com.interfacetype.sort.AbsArray;

import java.util.Arrays;

public class SupplyNodeslice extends AbsArray<SupplyNode> {

    private SupplyNode[] supplyNodes;

    private int size;

    public SupplyNodeslice(int count) {
        this.supplyNodes = new SupplyNode[count];
        this.size = 0;
    }

    public SupplyNodeslice(SupplyNode ... supplyNodes){
        this.supplyNodes = supplyNodes;
        this.size = supplyNodes.length;
    }

    public void add(SupplyNode supplyNode) {
        if (size == supplyNodes.length) {
            this.supplyNodes = resize(supplyNodes, size << 1);
        }
        supplyNodes[size++] = supplyNode;
    }

    public SupplyNode get(int index){
        return supplyNodes[index];
    }

    @Override
    public int len() {
        return size;
    }

    @Override
    public void swap(int i, int j) {
        SupplyNode tmp = supplyNodes[i];
        supplyNodes[i] = supplyNodes[j];
        supplyNodes[j] = tmp;
    }

    @Override
    public boolean less(int i, int j) {
        return supplyNodes[i].getRemainCnt() / supplyNodes[i].getSupplyCnt()
                < supplyNodes[j].getRemainCnt() / supplyNodes[j].getSupplyCnt();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(supplyNodes[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}
