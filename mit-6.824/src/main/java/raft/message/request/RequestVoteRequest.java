package raft.message.request;

public class RequestVoteRequest {
    //来源
    private int source;
    //目的
    private int destination;

    private long term;

    private long lastLogTerm;

    private long lastLogIndex;

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

    public long getTerm() {
        return term;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public long getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(long lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    public long getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(long lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    @Override
    public String toString() {
        return "RequestVoteRequest message [" +
                "source=" + source +
                ", destination=" + destination +
                ", term=" + term +
                ", lastLogTerm=" + lastLogTerm +
                ", lastLogIndex=" + lastLogIndex +
                ']';
    }
}
