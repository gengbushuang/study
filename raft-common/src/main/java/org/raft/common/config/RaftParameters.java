package org.raft.common.config;

public class RaftParameters {

	// 选举超时时间上限
	private int electionTimeoutUpperBound;
	// 选举超时时间下限
	private int electionTimeoutLowerBound;

	public int getElectionTimeoutUpperBound() {
		return electionTimeoutUpperBound;
	}

	public RaftParameters setElectionTimeoutUpperBound(int electionTimeoutUpperBound) {
		this.electionTimeoutUpperBound = electionTimeoutUpperBound;
		return this;
	}

	public int getElectionTimeoutLowerBound() {
		return electionTimeoutLowerBound;
	}

	public RaftParameters setElectionTimeoutLowerBound(int electionTimeoutLowerBound) {
		this.electionTimeoutLowerBound = electionTimeoutLowerBound;
		return this;
	}

}
