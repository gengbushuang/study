package com.retrieval.model;

import com.retrieval.util.PostingListComparable;

import java.util.Objects;

public class DocidNode implements PostingListComparable<DocidNode> {

    private final long uniqueId;

    private final boolean isNot;

    public DocidNode(long uniqueId, boolean isNot) {
        this.uniqueId = uniqueId;
        this.isNot = isNot;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public boolean isNot() {
        return isNot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocidNode docidNode = (DocidNode) o;
        return uniqueId == docidNode.uniqueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }

    @Override
    public int compareTo(DocidNode o) {
        return (int) (this.uniqueId - o.uniqueId);
    }

    @Override
    public int compareLess(DocidNode o) {
        if (this.isNot == o.isNot) {
            return (int) (this.uniqueId - o.uniqueId);
        } else {
            return this.isNot ? 1 : -1;
        }
    }

    @Override
    public String toString() {
        return "DocidNode{" +
                "uniqueId=" + uniqueId +
                ", isNot=" + isNot +
                '}';
    }
}
