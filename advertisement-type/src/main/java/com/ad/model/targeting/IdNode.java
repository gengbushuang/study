package com.ad.model.targeting;

public class IdNode extends TreeNode {

    private final long id;

    public IdNode(NodeTypeEnum nodeTypeEnum, long id) {
        super(nodeTypeEnum);
        this.id = id;
    }
}
