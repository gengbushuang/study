package raft;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
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

	private Set<Integer> votedServers = new HashSet<>();
	private int votesGranted;
	private boolean electionCompleted;

	private Random random;
	// 延迟任务
	private ScheduledFuture<?> scheduledElection;
	private ScheduledExecutorService scheduledExecutorService;

	private Callable<Void> electionTimeoutTask;

	public RaftService() {
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
		//清空
		this.votedServers.clear();
		//赞成的人数
		this.votesGranted = 0;
		//竞选状态
		this.electionCompleted = false;
		//TODO 状态写入
		//发起成为领导人请求
		this.requestVote();

	}

	private void requestVote() {
		this.state.setVotedFor(this.id);
		//人数加1
		this.votesGranted += 1;
		//添加服务ID
		this.votedServers.add(this.id);
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
