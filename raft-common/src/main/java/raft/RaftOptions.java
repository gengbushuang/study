package raft;

public class RaftOptions {
	
	//选举时间上界
	private int electionTimeoutUpperBound;
	//选举时间下界
    private int electionTimeoutLowerBound;
    //心跳时间
    private int heartbeatPeriodMilliseconds = 500;
	//最大追加日志大小
	private int maxAppendingSize;

	private int rpcFailureBackoff;

	private int snapshotDistance = 50;
	
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

	public void setMaxAppendingSize(int maxAppendingSize){
		this.maxAppendingSize = maxAppendingSize;
	}

	public int getMaximumAppendingSize(){
		return this.maxAppendingSize;
	}

	public int getMaxHeartbeatInterval() {
		return Math.max(this.heartbeatPeriodMilliseconds, this.electionTimeoutLowerBound - this.heartbeatPeriodMilliseconds / 2);
	}

	public void setRpcFailureBackoff(int rpcFailureBackoff) {
		this.rpcFailureBackoff = rpcFailureBackoff;
	}

	public int getRpcFailureBackoff() {
		return rpcFailureBackoff;
	}

	public int getSnapshotDistance() {
		return snapshotDistance;
	}

	public void setSnapshotDistance(int snapshotDistance) {
		this.snapshotDistance = snapshotDistance;
	}
}
