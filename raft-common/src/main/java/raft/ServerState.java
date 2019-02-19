package raft;

public class ServerState {

	// 服务器最后一次知道的任期号
	private long term;
	// 已知的最大的已经被提交的日志条件的索引值
	private long commitIndex;
	// 在当前获得选票的候选人的ID
	private int votedFor;

	public void increaseTerm(){
		this.term += 1;
	}

	public long getTerm() {
		return term;
	}

	public void setTerm(long term) {
		this.term = term;
	}

	public long getCommitIndex() {
		return commitIndex;
	}

	public void setCommitIndex(long commitIndex) {
		if (commitIndex > this.commitIndex) {
			this.commitIndex = commitIndex;
		}
	}

	public int getVotedFor() {
		return votedFor;
	}

	public void setVotedFor(int votedFor) {
		this.votedFor = votedFor;
	}

}
