package raft;

import java.util.*;
import java.util.concurrent.*;

public class RaftService {

	private RaftOptions raftOptions;

	private ServerRole role;
	//
	private ServerState state;
	// leader节点id
	private int leaderId;
	// 服务器节点id
	private int id;
	//除了自己其他服务节点
	private Map<Integer, PeerServer> peers = new HashMap<Integer, PeerServer>();

	private Set<Integer> votedServers = new HashSet<>();
	private int votesGranted;
	private boolean electionCompleted;

	private Random random;
	// 延迟任务
	private ScheduledFuture<?> scheduledElection;
	private ScheduledExecutorService scheduledExecutorService;

	private Callable<Void> electionTimeoutTask;

	public RaftService() {
		//投票数
		this.votesGranted = 0;
		//leaderID
		this.leaderId = -1;
		//选举状态
		this.electionCompleted = false;


		// 创建随机对象
		this.random = new Random(Calendar.getInstance().getTimeInMillis());

		this.electionTimeoutTask = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				handleElectionTimeout();
				return null;
			}
		};
		scheduledExecutorService = Executors.newScheduledThreadPool(2);

		//转换follower角色状态
		this.role = ServerRole.Follower;
	}

	/**
	 * 选举请求
	 */
	void handleVoteRequest(){

	}

	//
	private synchronized void handleElectionTimeout() {



		//如果当前是领导人
		if(this.role == ServerRole.Leader){
			//TODO 这里有状态方法没有弄
			return;
		}
		//转变为Candidate，进行请求投票
		this.state.increaseTerm();
		this.state.setVotedFor(-1);
		this.role = ServerRole.Candidate;
		//TODO 状态写入
		//清空
		this.votedServers.clear();
		//赞成的人数
		this.votesGranted = 0;
		//竞选状态
		this.electionCompleted = false;
		//发起成为领导人请求
		this.requestVote();

	}

	private void requestVote() {
		this.state.setVotedFor(this.id);
		//TODO 状态写入
		//人数加1
		this.votesGranted += 1;
		//添加服务ID
		this.votedServers.add(this.id);

		//如果只有一个机器服务
		if (this.votesGranted > (this.peers.size() + 1) / 2) {
			this.electionCompleted = true;
			this.becomeLeader();
			return;
		}
	}

	private void becomeLeader(){
		//先停止选举定时
		this.stopElectionTimer();
		//转换为领导者
		this.role = ServerRole.Leader;
		//设置领导者ID
		this.leaderId = this.id;

		//
		for(PeerServer peerServer:this.peers.values()){
			//TODO 待补充
			//开启心跳
			this.enableHeartbeatForPeer(peerServer);
		}

		//TODO 待补充

		this.requestAppendEntries();

	}

	private void requestAppendEntries() {

		if(this.peers.size() == 0){
			//TODO 待补充
			return;
		}

		for(PeerServer peer : this.peers.values()){
			this.requestAppendEntries(peer);
		}

	}

	private void requestAppendEntries(PeerServer peer) {
		long currentNextIndex = 0;
		long commitIndex = 0;
		long lastLogIndex = 0;
		long term = 0;
		long startingIndex = 1;
	}

	private void enableHeartbeatForPeer(PeerServer peerServer) {
		peerServer.enableHeartbeat(true);
		peerServer.resumeHeartbeatingSpeed();
		//设置心跳任务
		ScheduledFuture<Void> schedule = scheduledExecutorService.schedule(peerServer.getHeartbeartHandler(), peerServer.getCurrentHeartbeatInterval(), TimeUnit.MILLISECONDS);
		peerServer.setHeartbeatTask(schedule);
	}

	/**
	 * 选举定时器
	 */
	private void restartElectionTimer() {
		// 如果不为null，取消以前的任务
		if (this.scheduledElection != null) {
			this.scheduledElection.cancel(false);
		}
		// 重新构建定时任务
		int electionTimeout = raftOptions.getElectionTimeoutLowerBound() + this.random.nextInt(raftOptions.getElectionTimeoutUpperBound() - raftOptions.getElectionTimeoutLowerBound() + 1);
		this.scheduledElection = scheduledExecutorService.schedule(this.electionTimeoutTask, electionTimeout, TimeUnit.MILLISECONDS);
	}

	/**
	 * 停止定时选举
	 */
	private void stopElectionTimer() {
		if (this.scheduledElection == null) {
			return;
		}
		this.scheduledElection.cancel(false);
		this.scheduledElection = null;
	}
}
