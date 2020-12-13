package com.ad.indexer;

import com.ad.model.targeting.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AdProcessor {

    public Tree convertUserTargeting(String targetingLimit) {
        Tree.TreeBuilder builder = Tree.builder();
        builder.logicalOpEnum(LogicalOpEnum.AND);
        if(StringUtils.isBlank(targetingLimit)){
            return builder.build();
        }

        String[] targetingLimitArray = StringUtils.split(targetingLimit, ";");
        List<Tree> trees = new ArrayList<>(targetingLimitArray.length);
        for (String target : targetingLimitArray) {
            Tree.TreeBuilder subbuilder = Tree.builder();

            String[] targetArrays = StringUtils.split(target, ",");
            List<TreeNode> treeNodes = new ArrayList<>(targetArrays.length);
            for (String tag : targetArrays) {
                int indexOf = StringUtils.indexOf(tag, "_");
                TreeNode treeNode;
                if (indexOf > -1) {
                    long low = Long.parseLong(StringUtils.substring(tag, 0, indexOf));
                    long high = Long.parseLong(StringUtils.substring(tag, indexOf + 1));
                    treeNode = new RangeNode(NodeTypeEnum.RANGE, low, high);
                } else {
                    long id = Long.parseLong(tag);
                    treeNode = new IdNode(NodeTypeEnum.ID, id);
                }
                treeNodes.add(treeNode);
            }
            subbuilder.treeNodeList( new TreeNodeList(treeNodes));
            trees.add(subbuilder.build());
        }
        builder.trees(trees);
        return builder.build();
    }
}
