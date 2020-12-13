package raft.message.response;

public class AppendEntriesResponse {

    //来源
    private int source;
    //目的
    private int destination;

    private int term;

    private boolean success;

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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getLastLogIndex() {
        return lastLogIndex;
    }

    public void setLastLogIndex(int lastLogIndex) {
        this.lastLogIndex = lastLogIndex;
    }

    @Override
    public String toString() {
        return "AppendEntriesResponse message [" +
                "source=" + source +
                ", destination=" + destination +
                ", term=" + term +
                ", success=" + success +
                ", lastLogIndex=" + lastLogIndex +
                ']';
    }
}
