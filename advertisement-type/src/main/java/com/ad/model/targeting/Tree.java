package com.ad.model.targeting;

import lombok.Builder;

import java.util.List;

@Builder
public class Tree {

    private boolean not;

    private LogicalOpEnum logicalOpEnum;

    private TreeNodeList treeNodeList;

    private List<Tree> trees;
}
