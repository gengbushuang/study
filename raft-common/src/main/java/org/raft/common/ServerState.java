package org.raft.common;
public class ServerState {
	//服务器最后一次任期号
    private long term;
    //已知的最大的已经被提交的日志条目的索引值
    private long commitIndex;
    //在当前获得选票的候选人的 Id
    private int votedFor;

    public long getTerm() {
        return term;
    }

    public void setTerm(long term) {
        this.term = term;
    }

    public int getVotedFor() {
        return votedFor;
    }

    public void setVotedFor(int votedFor) {
        this.votedFor = votedFor;
    }
    
    public void increaseTerm(){
        this.term += 1;
    }

    public long getCommitIndex() {
        return commitIndex;
    }

    public void setCommitIndex(long commitIndex) {
        if(commitIndex > this.commitIndex){
            this.commitIndex = commitIndex;
        }
    }
}