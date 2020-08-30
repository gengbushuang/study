package raft;

public class RaftState {

    private long term;

    private long votedFor;

    public long getTerm() {
        return term;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public long getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(long votedFor) {
        this.votedFor = votedFor;
    }
}
