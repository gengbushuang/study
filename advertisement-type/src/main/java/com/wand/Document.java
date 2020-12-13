package com.wand;

import java.util.Comparator;

public class Document implements Comparable<Document> {
    private int docid;
    private double score;

    public Document (int docid, double score) {
        this.docid = docid;
        this.score = score;
    }

    public int getDocid() {
        return docid;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Document{" +
                "docid=" + docid +
                ", score=" + score +
                '}';
    }

    @Override
    public int compareTo(Document o) {
        int retVal;
        double scoreDifference = this.getScore() - o.getScore();
        if (scoreDifference == 0)
            retVal = 0;
        else if (scoreDifference > 0)
            retVal = -1;
        else
            retVal = 1;

        return retVal;
    }
}
