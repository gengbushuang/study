package org.raft.common.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RaftContext {

	private ScheduledThreadPoolExecutor scheduledExecutor;

	private RaftParameters raftParameters;

	public RaftContext(RaftParameters raftParameters) {

		this.raftParameters = raftParameters;

		if (this.scheduledExecutor == null) {
			this.scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
		}
		if (this.raftParameters == null) {
			this.raftParameters = new RaftParameters()
			.setElectionTimeoutLowerBound(3000)
			.setElectionTimeoutUpperBound(5000);
		}

	}

	public ScheduledThreadPoolExecutor getScheduledExecutor() {
		return scheduledExecutor;
	}

	public RaftParameters getRaftParameters() {
		return raftParameters;
	}

}
