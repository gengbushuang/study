package raft.message.request;

import raft.message.Entry;

public class AppendEntriesRequest {
    //来源
    private int source;
    //目的
    private int destination;

    private int term;

    private int prevLogIndex;

    private int prevLogTerm;

    private Entry entry;

    private int commitIndex;

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getPrevLogIndex() {
        return prevLogIndex;
    }

    public void setPrevLogIndex(int prevLogIndex) {
        this.prevLogIndex = prevLogIndex;
    }

    public int getPrevLogTerm() {
        return prevLogTerm;
    }

    public void setPrevLogTerm(int prevLogTerm) {
        this.prevLogTerm = prevLogTerm;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public int getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(int commitIndex) {
        this.commitIndex = commitIndex;
    }

    @Override
    public String toString() {
        return "AppendEntriesRequest message [" +
                "source=" + source +
                ", destination=" + destination +
                ", term=" + term +
                ", prevLogIndex=" + prevLogIndex +
                ", prevLogTerm=" + prevLogTerm +
                ", entry=" + entry +
                ", commitIndex=" + commitIndex +
                ']';
    }
}
