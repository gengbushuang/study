package com.ad.model.targeting;

import java.util.Iterator;

public abstract class TreeNode {

    private final NodeTypeEnum nodeTypeEnum;

    protected TreeNode(NodeTypeEnum nodeTypeEnum){
        this.nodeTypeEnum = nodeTypeEnum;
    }

    public NodeTypeEnum getNodeTypeEnum() {
        return nodeTypeEnum;
    }

}
