package raft;

public class RaftOptions {
	
	//选举时间
	private int electionTimeoutUpperBound;
    private int electionTimeoutLowerBound;
    //心跳时间
    private int heartbeatPeriodMilliseconds = 500;
	
	public int getElectionTimeoutUpperBound() {
		return electionTimeoutUpperBound;
	}
	public void setElectionTimeoutUpperBound(int electionTimeoutUpperBound) {
		this.electionTimeoutUpperBound = electionTimeoutUpperBound;
	}

	public int getElectionTimeoutLowerBound() {
		return electionTimeoutLowerBound;
	}
	public void setElectionTimeoutLowerBound(int electionTimeoutLowerBound) {
		this.electionTimeoutLowerBound = electionTimeoutLowerBound;
	}
	
	public int getHeartbeatPeriodMilliseconds() {
		return heartbeatPeriodMilliseconds;
	}
	public void setHeartbeatPeriodMilliseconds(int heartbeatPeriodMilliseconds) {
		this.heartbeatPeriodMilliseconds = heartbeatPeriodMilliseconds;
	}
    
}
