package raft.message.request;

public class RequestVoteRequest {
    //来源
    private int source;
    //目的
    private int destination;

    private int term;

    private int lastLogTerm;

    private int lastLogIndex;

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

    public int getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(int lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }

    public int getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(int lastLogIndex) {
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
