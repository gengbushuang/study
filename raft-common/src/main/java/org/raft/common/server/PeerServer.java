package org.raft.common.server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;

import org.raft.common.client.RpcTcpClient;
import org.raft.common.config.RaftContext;
import org.raft.common.exception.RpcException;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;

public class PeerServer {

	private RpcTcpClient rpcClient;

	private Executor executor;

	private ClusterServer clusterConfig;

	private int currentHeartbeatInterval;
	// 心跳任务
	private ScheduledFuture<?> heartbeatTask;

	private boolean heartbeatEnabled;

	public PeerServer(ClusterServer server, RaftContext context) {
		this.clusterConfig = server;

		this.executor = context.getScheduledExecutor();

		this.rpcClient = context.getRpcClientFactory().createRpcClient(server.getEndpoint());

		this.heartbeatTask = null;
	}

	public int getId() {
		return this.clusterConfig.getId();
	}

	public ScheduledFuture<?> getHeartbeatTask() {
		return heartbeatTask;
	}

	public void setHeartbeatTask(ScheduledFuture<?> heartbeatTask) {
		this.heartbeatTask = heartbeatTask;
	}

	public boolean isHeartbeatEnabled() {
		return heartbeatEnabled;
	}

	public void setHeartbeatEnabled(boolean heartbeatEnabled) {
		this.heartbeatEnabled = heartbeatEnabled;
		//false把心跳定时任务设置为null
		if (!heartbeatEnabled) {
			this.heartbeatTask = null;
		}
	}

	public CompletableFuture<RaftResponseMessage> SendRequest(RaftRequestMessage request) {
		CompletableFuture<RaftResponseMessage> future = this.rpcClient.send(request);

		return future.thenComposeAsync((RaftResponseMessage response) -> {

			return CompletableFuture.completedFuture(response);
		}, this.executor).exceptionally((Throwable error) -> {
			throw new RpcException(error, request);
		});
	}
}
