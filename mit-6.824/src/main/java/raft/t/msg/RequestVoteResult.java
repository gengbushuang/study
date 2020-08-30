package raft.t.msg;

public class RequestVoteResult {
    private final int term;
    private final boolean voteGranted;

    public RequestVoteResult(int term,boolean voteGranted){
        this.term =term;
        this.voteGranted = voteGranted;
    }

    public int getTerm() {
        return term;
    }

    public boolean isVoteGranted() {
        return voteGranted;
    }
}
