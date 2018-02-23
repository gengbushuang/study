package org.raft.common.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.raft.common.client.RpcTcpClientFactory;

public class RaftContext {

	private ScheduledThreadPoolExecutor scheduledExecutor;

	private RaftParameters raftParameters;
	
	private RpcTcpClientFactory rpcClientFactory;

	public RaftContext(RaftParameters raftParameters,RpcTcpClientFactory rpcClientFactory) {

		this.raftParameters = raftParameters;
		this.rpcClientFactory = rpcClientFactory;

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

	public RpcTcpClientFactory getRpcClientFactory() {
		return rpcClientFactory;
	}

}
