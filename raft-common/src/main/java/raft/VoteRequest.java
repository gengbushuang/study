package raft;

public class VoteRequest {

	private int serverId;//请求选票的候选人的ID
	private long term;//候选人的任期号
	private long lastLogTerm;//候选人的最后日志条目的任期号
	private long lastLogIndex;//候选人最后日志条目的索引值

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
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
}
