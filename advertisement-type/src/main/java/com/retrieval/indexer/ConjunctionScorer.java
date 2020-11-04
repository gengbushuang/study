package com.retrieval.indexer;

import com.retrieval.indexer.model.DocidNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 有机会在优化一下
 */
public class ConjunctionScorer {

    private final static Comparator<PostingList> comparator_default = new Comparator<PostingList>() {
        @Override
        public int compare(PostingList o1, PostingList o2) {
            if (o1.first() == null) {
                return 1;
            }
            if (o2.first() == null) {
                return -1;
            }
            int tmp = o1.first().getDocid() - o2.first().getDocid();
            if (tmp == 0) {
                if (o1.first().isBt() != o2.first().isBt()) {
                    if (o1.first().isBt()) {
                        tmp = 1;
                    } else {
                        tmp = -1;
                    }
                }
            }
            return tmp;
        }
    };

    private Comparator<PostingList> comparator;

    private List<PostingList> postingLists;

    public ConjunctionScorer() {
        this(comparator_default);
    }

    public ConjunctionScorer(Comparator<PostingList> comparator) {
        this.comparator = comparator;
        this.postingLists = new ArrayList<>();
    }

    public void addPostingList(DocidNode[] docidNodes) {
        this.postingLists.add(new PostingList(docidNodes));
    }

    public void addPostingList(PostingList postingList) {
        this.postingLists.add(postingList);
    }

    public List<Integer> getDocIds(int size) {
        if (postingLists.size() < size) {
            return Collections.EMPTY_LIST;
        }

        if (postingLists.size() == size) {
            return handlerEQ(size);
        } else {
            return handlerGT(size);
        }
    }

    private List<Integer> handlerEQ(int size) {
        List<Integer> resutl = new ArrayList<>(size);
        postingLists.sort(comparator);
        int lastDocid = 0;
        while ((lastDocid = nextDocId()) > -1) {
            resutl.add(lastDocid);
            postingLists.get(size - 1).nextDoc();
        }
        return resutl;
    }

    private int nextDocId() {
        int first = 0;
        int len = postingLists.size();
        PostingList postingList = postingLists.get(first);
        PostingList postingEnd = postingLists.get(len - 1);
        if (postingEnd.first() == null) {
            return -1;
        }
        int docId = postingEnd.first().getDocid();
        while (postingList.first().getDocid() < docId || !postingList.first().isBt()) {
            docId = postingList.advance(docId);
            if (docId == -1) {
                break;
            }
            first = first == len - 1 ? 0 : first + 1;
            postingList = postingLists.get(first);
        }

        return docId;
    }


    private List<Integer> handlerGT(int size) {
        List<Integer> resutl = new ArrayList<>(size);
        int nextDocId = 0;
        while (postingLists.get(size - 1).first() != null) {
            postingLists.sort(comparator);
            PostingList postingFirst = postingLists.get(0);
            PostingList postingEnd = postingLists.get(size - 1);
            if (postingFirst.first().getDocid() == postingEnd.first().getDocid()) {

                nextDocId = postingEnd.first().getDocid() + 1;

                if (postingFirst.first().isBt()) {
                    resutl.add(postingEnd.first().getDocid());
                }
                for (int i = size; i < postingLists.size(); ++i) {
                    //因为是排好序的，如果有null或者大于就直接退出
                    if (postingLists.get(i) == null || postingLists.get(i).first().getDocid() >= nextDocId) {
                        break;
                    }
                    postingLists.get(i).skipToDocId(nextDocId);
                }

            } else {
                nextDocId = postingEnd.first().getDocid();
            }

            for (int i = 0; i < size; i++) {
                postingLists.get(i).skipToDocId(nextDocId);
            }
        }
        return resutl;
    }
}
