package com.retrieval.test;

import com.retrieval.model.DocidNode;
import com.retrieval.util.ConjunctionScorer;
import com.retrieval.util.PostingList;

public class ConjunctionScorerTest {
    public static void main(String[] args) {
        ConjunctionScorer scorer = new ConjunctionScorer();
        PostingList<DocidNode> docidNodes1 = new PostingList();
        docidNodes1.add(new DocidNode(1,true));
        docidNodes1.add(new DocidNode(10,true));
        docidNodes1.add(new DocidNode(11,true));
        docidNodes1.add(new DocidNode(12,true));
        docidNodes1.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes1.iterator());


        PostingList<DocidNode> docidNodes2 = new PostingList();
        docidNodes2.add(new DocidNode(8,true));
        docidNodes2.add(new DocidNode(11,true));
        docidNodes2.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes2.iterator());


        PostingList<DocidNode> docidNodes3 = new PostingList();
        docidNodes3.add(new DocidNode(4,true));
        docidNodes3.add(new DocidNode(9,true));
        docidNodes3.add(new DocidNode(10,true));
        docidNodes3.add(new DocidNode(11,true));
        docidNodes3.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes3.iterator());

        PostingList<DocidNode> docidNodes4 = new PostingList();
        docidNodes4.add(new DocidNode(8,true));
        docidNodes4.add(new DocidNode(9,true));
        docidNodes4.add(new DocidNode(11,true));
        docidNodes4.add(new DocidNode(12,true));
        docidNodes4.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes4.iterator());

        PostingList<DocidNode> docidNodes5 = new PostingList();
        docidNodes5.add(new DocidNode(1,true));
        docidNodes5.add(new DocidNode(9,false));
        docidNodes5.add(new DocidNode(11,true));
        docidNodes5.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes5.iterator());

        PostingList<DocidNode> docidNodes6 = new PostingList();
        docidNodes6.add(new DocidNode(7,true));
        docidNodes6.add(new DocidNode(11,false));
        docidNodes6.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes6.iterator());

        PostingList<DocidNode> docidNodes7 = new PostingList();
        docidNodes7.add(new DocidNode(4,true));
        docidNodes7.add(new DocidNode(5,true));
        docidNodes7.add(new DocidNode(9,true));
        docidNodes7.add(new DocidNode(11,true));
        docidNodes7.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes7.iterator());

        PostingList<DocidNode> docidNodes8 = new PostingList();
        docidNodes8.add(new DocidNode(1,true));
        docidNodes8.add(new DocidNode(11,true));
        docidNodes8.add(new DocidNode(12,true));
        docidNodes8.add(new DocidNode(13,true));
        scorer.addPostingList(docidNodes8.iterator());

        scorer.getDocIds(4);
    }
}
