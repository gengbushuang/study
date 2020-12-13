package com.ad.model.targeting;

import java.util.Iterator;
import java.util.List;

public class TreeNodeList implements Iterable<TreeNode> {

    private List<TreeNode> children;

    private boolean not;

    public TreeNodeList(List<TreeNode> children) {
        this.children = children;
    }

    @Override
    public Iterator<TreeNode> iterator() {
        return children.iterator();
    }
}
