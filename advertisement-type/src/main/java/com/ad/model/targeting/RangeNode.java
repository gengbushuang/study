package com.ad.model.targeting;

public class RangeNode extends TreeNode {

    private final long begin;

    private final long end;

    public RangeNode(NodeTypeEnum nodeTypeEnum, long low, long high) {
        super(nodeTypeEnum);
        this.begin = low;
        this.end = high;
    }

}
