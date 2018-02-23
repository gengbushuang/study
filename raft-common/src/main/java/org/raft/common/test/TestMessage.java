package org.raft.common.test;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.raft.common.ServerRole;
import org.raft.common.ServerState;
import org.raft.common.config.RaftContext;
import org.raft.common.config.RaftParameters;
import org.raft.common.db.H2LogStore;
import org.raft.common.message.RaftMessageHandler;
import org.raft.common.message.RaftMessageType;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;
import org.raft.common.server.PeerServer;

public class TestMessage implements RaftMessageHandler {

	private ScheduledFuture<?> scheduledElection;

	private RaftContext context;

	private Logger logger;

	private int id;
	private int leader;

	private ServerState state;
	// 角色
	private ServerRole role;

	private boolean electionCompleted;

	private int votesResponded;
	private int votesGranted;

	private boolean catchingUp = false;

	private Callable<Void> electionTimeoutTask;

	private Random random;

	private Map<Integer, PeerServer> peers = new HashMap<Integer, PeerServer>();

	private H2LogStore logStore;

	private long quickCommitIndex;

	public TestMessage() {
		this.logger = LogManager.getLogger(getClass());

		this.electionCompleted = false;

		this.votesGranted = 0;
		this.votesResponded = 0;
		// 构造一个固定的随机种子，伪随机
		this.random = new Random(Calendar.getInstance().getTimeInMillis());

		// 选举定时任务
		this.electionTimeoutTask = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				// handleElectionTimeout();
				return null;
			}
		};

		if (this.state == null) {
			this.state = new ServerState();
			this.state.setTerm(0);
			this.state.setVotedFor(-1);
			this.state.setCommitIndex(0);
		}

		// peers.put(key, value)

		this.quickCommitIndex = this.state.getCommitIndex();
		this.role = ServerRole.Follower;
		this.restartElectionTimer();
		this.logger.info("Server %d started", this.id);
	}

	@Override
	public RaftResponseMessage processRequest(RaftRequestMessage request) {
		this.logger.debug("Receive a %s message from %d with LastLogIndex=%d, LastLogTerm=%d, EntriesLength=%d, CommitIndex=%d and Term=%d", request.getMessageType().toString(), request.getSource(),
				request.getLastLogIndex(), request.getLastLogTerm(), request.getLogEntries() == null ? 0 : request.getLogEntries().length, request.getCommitIndex(), request.getTerm());
		RaftResponseMessage response = null;
		if (request.getMessageType() == RaftMessageType.AppendEntriesRequest) {
			response = this.handleAppendEntriesRequest(request);
		} else if (request.getMessageType() == RaftMessageType.RequestVoteRequest) {
			response = this.handleVoteRequest(request);
		} else if (request.getMessageType() == RaftMessageType.ClientRequest) {
			response = this.handleClientRequest(request);
		} else {
			response = this.handleExtendedMessages(request);
		}
		if (response != null) {
			this.logger.debug("Response back a %s message to %d with Accepted=%s, Term=%d, NextIndex=%d", response.getMessageType().toString(), response.getDestination(),
					String.valueOf(response.isAccepted()), response.getTerm(), response.getNextIndex());
		}

		return response;
	}

	private synchronized RaftResponseMessage handleAppendEntriesRequest(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.AppendEntriesResponse);
		response.setNextIndex(2);
		return response;
	}

	// 收到投票请求 RPC
	private synchronized RaftResponseMessage handleVoteRequest(RaftRequestMessage request) {
		// 候选人的任期号
		this.updateTerm(request.getTerm());

		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.RequestVoteResponse);
		// 来源
		response.setSource(this.id);
		// 目标
		response.setDestination(request.getSource());
		// 目前的任期号
		response.setTerm(this.state.getTerm());

		return response;
	}

	private synchronized RaftResponseMessage handleExtendedMessages(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.LeaveClusterResponse);
		response.setNextIndex(4);
		return response;
	}

	// 构建投票请求
	private void requestVote() {
		// 设置候选人ID
		this.state.setVotedFor(this.id);

		this.votesGranted += 1;
		this.votesResponded += 1;

		// 向其他机器发送选举投票请求
		for (PeerServer peer : this.peers.values()) {
			RaftRequestMessage request = new RaftRequestMessage();
			request.setMessageType(RaftMessageType.RequestVoteRequest);
			// 目标ID
			request.setDestination(peer.getId());
			// 来源
			request.setSource(this.id);
			// 相当于最大ID
			request.setLastLogIndex(this.logStore.getFirstAvailableIndex() - 1);
			// 日志的任期号
			request.setLastLogTerm(this.termForLastLog(this.logStore.getFirstAvailableIndex() - 1));
			request.setTerm(this.state.getTerm());

			peer.SendRequest(request).whenCompleteAsync((RaftResponseMessage response, Throwable error) -> {
				handlePeerResponse(response, error);
			}, context.getScheduledExecutor());
		}
	}

	//
	private synchronized void handlePeerResponse(RaftResponseMessage response, Throwable error) {
		if (error != null) {
			this.logger.info("peer response error: %s", error.getMessage());
			return;
		}

		if (this.updateTerm(response.getTerm())) {
			return;
		}

		if (response.getTerm() < this.state.getTerm()) {
			this.logger.info("Received a peer response from %d that with lower term value %d v.s. %d", response.getSource(), response.getTerm(), this.state.getTerm());
			return;
		}
		if (response.getMessageType() == RaftMessageType.RequestVoteResponse) {
			this.handleVotingResponse(response);
		}

	}

	private void handleVotingResponse(RaftResponseMessage response) {
		this.votesResponded += 1;
		if (this.electionCompleted) {
			this.logger.info("Election completed, will ignore the voting result from this server");
			return;
		}
		// 正确响应累计
		if (response.isAccepted()) {
			this.votesGranted += 1;
		}
		//
		if (this.votesResponded >= this.peers.size() + 1) {
			this.electionCompleted = true;
		}
		// 过半响应成功角色转变为领导者
		if (this.votesGranted > (this.peers.size() + 1) / 2) {
			this.logger.info("Server is elected as leader for term %d", this.state.getTerm());
			this.electionCompleted = true;
			this.becomeLeader();
		}

	}

	// 获取日志的任期号
	private long termForLastLog(long logIndex) {
		if (logIndex == 0) {
			return 0;
		}
		if (logIndex >= this.logStore.getStartIndex()) {
			return this.logStore.getLogEntryAt(logIndex).getTerm();
		}
		return 0;
	}

	// 更新任期号
	private boolean updateTerm(long term) {
		if (term > this.state.getTerm()) {
			// 更新任期号
			this.state.setTerm(term);
			this.state.setVotedFor(-1);
			this.votesGranted = 0;
			this.votesResponded = 0;
			// 保存状态 还没有写

			// 调用追随者设置
			this.becomeFollower();
			return true;
		}
		return false;
	}

	// 追随者相关设置
	private void becomeFollower() {
		for(PeerServer server : this.peers.values()){
			 if(server.getHeartbeatTask() != null){
				 //中断心跳任务
				 server.getHeartbeatTask().cancel(false);
			 }
			 server.setHeartbeatEnabled(false);
		}
		// 改变角色状态为追随者
		this.role = ServerRole.Follower;
		// 重置定时选举
		this.restartElectionTimer();
	}

	// 领导者相关设置
	private void becomeLeader() {
		this.stopElectionTimer();
		this.role = ServerRole.Leader;
		this.leader = this.id;

		for (PeerServer server : this.peers.values()) {
			
		}
	}

	private void restartElectionTimer() {
		if (this.catchingUp) {
			return;
		}

		// 如果不null，中断以前的定时任务
		if (this.scheduledElection != null) {
			this.scheduledElection.cancel(false);
		}
		// 重新设置定时任务
		RaftParameters raftParameters = this.context.getRaftParameters();
		// 超时上限和下限的差值
		int n = (raftParameters.getElectionTimeoutUpperBound() - raftParameters.getElectionTimeoutLowerBound() + 1);
		// 超时下限加上 随机[0-差值]范围
		int electionTimeout = raftParameters.getElectionTimeoutLowerBound() + this.random.nextInt(n);
		this.scheduledElection = context.getScheduledExecutor().schedule(electionTimeoutTask, electionTimeout, TimeUnit.MILLISECONDS);
	}

	// 停止选举定时任务
	private void stopElectionTimer() {
		if (this.scheduledElection == null) {
			this.logger.warn("Election Timer is never started but is requested to stop, protential a bug");
			return;
		}

		this.scheduledElection.cancel(false);
		this.scheduledElection = null;
	}

	private RaftResponseMessage handleClientRequest(RaftRequestMessage request) {
		RaftResponseMessage response = new RaftResponseMessage();
		response.setMessageType(RaftMessageType.ClientRequest);
		response.setNextIndex(3);
		return response;
	}
}
