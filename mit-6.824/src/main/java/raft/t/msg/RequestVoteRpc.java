package raft.t.msg;

public class RequestVoteRpc {
    private int term;
    private int candidateId;
    private int lastLogindex = 0;//候选人最后一条日志索引
    private int lastLogTerm = 0;//候选人最后一条日志的term

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public int getLastLogindex() {
        return lastLogindex;
    }

    public void setLastLogindex(int lastLogindex) {
        this.lastLogindex = lastLogindex;
    }

    public int getLastLogTerm() {
        return lastLogTerm;
    }

    public void setLastLogTerm(int lastLogTerm) {
        this.lastLogTerm = lastLogTerm;
    }
}
