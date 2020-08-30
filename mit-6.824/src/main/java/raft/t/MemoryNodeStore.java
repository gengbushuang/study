package raft.t;

public class MemoryNodeStore implements NodeStore {
    private int term;
    private int votedFor;

    public MemoryNodeStore() {
        this(0, -1);
    }

    public MemoryNodeStore(int term, int votedFor) {
        this.term = term;
        this.votedFor = votedFor;
    }

    @Override
    public int getTrem() {
        return term;
    }

    @Override
    public void setTerm(int term) {
        this.term = term;
    }

    @Override
    public int getVotedFor() {
        return votedFor;
    }

    @Override
    public void setVotedFor(int votedFor) {
        this.votedFor = votedFor;
    }

    @Override
    public void close() {

    }
}
