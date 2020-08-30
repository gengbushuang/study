package raft.message.response;

public class RequestVoteResponse {
    //来源
    private int source;
    //目的
    private int destination;

    private long term;

    private boolean granted;

    private boolean logOk;

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

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public boolean isLogOk() {
        return logOk;
    }

    public void setLogOk(boolean logOk) {
        this.logOk = logOk;
    }

    @Override
    public String toString() {
        return "RequestVoteResponse message[" +
                "source=" + source +
                ", destination=" + destination +
                ", term=" + term +
                ", granted=" + granted +
                ", logOk=" + logOk +
                ']';
    }
}
