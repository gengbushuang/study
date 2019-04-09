package raft;

public class RaftResponseMessage extends RaftMessage {

    private long nextIndex;

    private boolean granted;//候选人赢得了此张选票时为真

    public boolean isGranted() {
        return granted;
    }

    public void setGranted(boolean granted) {
        this.granted = granted;
    }

    public long getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(long nextIndex) {
        this.nextIndex = nextIndex;
    }
}
