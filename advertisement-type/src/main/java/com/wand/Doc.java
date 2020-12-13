package com.wand;

public class Doc {

    private String[] keyword;

    private int docId;

    public Doc(int docId, String[] keyword) {
        this.docId = docId;
        this.keyword = keyword;
    }

    public String[] getKeyword() {
        return keyword;
    }

    public int getDocId() {
        return docId;
    }
}
