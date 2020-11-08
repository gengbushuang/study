package com.retrieval.util;

import com.retrieval.indexer.model.DocidNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 有机会在优化一下
 */
public class ConjunctionScorer {

    private final static Comparator<PostingListIterator<DocidNode>> comparator_default = new Comparator<PostingListIterator<DocidNode>>() {

        @Override
        public int compare(PostingListIterator<DocidNode> o1, PostingListIterator<DocidNode> o2) {
            if (o1.curEntry() != null && o2.curEntry() != null) {
                return o1.curEntry().compareLess(o2.curEntry());
            }
            return o1.curEntry() == null ? 1 : -1;

        }
    };

    private Comparator<PostingListIterator<DocidNode>> comparator;

    private List<PostingListIterator<DocidNode>> postingLists;

    //
    public ConjunctionScorer() {
        this(comparator_default);
    }

    public ConjunctionScorer(Comparator<PostingListIterator<DocidNode>> comparator) {
        this.comparator = comparator;
        this.postingLists = new ArrayList<>();
    }

    public void addPostingList(PostingListIterator<DocidNode> postingList) {
        this.postingLists.add(postingList);
    }

    //
    public List<Long> getDocIds(int size) {
        int topK = size;
        if (topK == 0) {
            topK = 1;
        }

        if (postingLists.size() < topK) {
            return Collections.EMPTY_LIST;
        }

        if (postingLists.size() == topK) {
            return handlerEQ(topK);
        } else {
            return handlerGT(topK);
        }
    }

    //
    private List<Long> handlerEQ(int size) {
        List<Long> resutl = new ArrayList<>(size);
        postingLists.sort(comparator);
        DocidNode lastDocid = null;
        while ((lastDocid = nextDocId()) != null) {
            resutl.add(lastDocid.getUniqueId());
            postingLists.get(size - 1).next();
        }
        return resutl;
    }

    private DocidNode nextDocId() {
        int first = 0;
        int len = postingLists.size();
        PostingListIterator<DocidNode> postingList = postingLists.get(first);
        PostingListIterator<DocidNode> postingEnd = postingLists.get(len - 1);
        if (postingEnd.curEntry() == null) {
            return null;
        }

        DocidNode docidNode = postingEnd.curEntry();
        while (postingList.curEntry().compareLess(docidNode) < 0) {
            postingList.seekTo(docidNode);
            docidNode = postingList.curEntry();
            if (docidNode == null) {
                break;
            }
            first = first == len - 1 ? 0 : first + 1;
            postingList = postingLists.get(first);
        }
        return docidNode;
    }


    private List<Long> handlerGT(int size) {
        List<Long> resutl = new ArrayList<>(size);
        long nextDocId = 0;
        boolean isNot = true;
        while (postingLists.get(size - 1).curEntry() != null) {
            postingLists.sort(comparator);
            PostingListIterator<DocidNode> postingFirst = postingLists.get(0);
            PostingListIterator<DocidNode> postingEnd = postingLists.get(size - 1);
            if (postingFirst.curEntry().getUniqueId() == postingEnd.curEntry().getUniqueId()) {
                nextDocId = postingEnd.curEntry().getUniqueId() + 1;
                isNot = postingEnd.curEntry().isNot();

                if (postingFirst.curEntry().isNot()) {
                    resutl.add(postingEnd.curEntry().getUniqueId());
                }
                for (int i = size; i < postingLists.size(); ++i) {
                    //因为是排好序的，如果有null或者大于就直接退出
                    if (postingLists.get(i) == null || postingLists.get(i).curEntry().getUniqueId() >= nextDocId) {
                        break;
                    }
                    postingLists.get(i).seekTo(new DocidNode(nextDocId, isNot));
                }

            } else {
                nextDocId = postingEnd.curEntry().getUniqueId();
                isNot = postingEnd.curEntry().isNot();
            }

            for (int i = 0; i < size; i++) {
                postingLists.get(i).seekTo(new DocidNode(nextDocId, isNot));
            }
        }
        return resutl;
    }
}
