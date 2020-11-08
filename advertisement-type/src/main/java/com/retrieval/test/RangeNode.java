package com.retrieval.test;

public class RangeNode {

    private final TokenRange range;

    private final long center;

    private RangeNode left;

    private RangeNode right;

    public RangeNode(TokenRange range){
        this.range = range;
        //中位数
        this.center = (range.getRight()-range.getLeft())/2+range.getLeft();
    }

    public long getCenter() {
        return center;
    }

    public RangeNode getLeft() {
        return left;
    }

    public RangeNode getRight() {
        return right;
    }
}
