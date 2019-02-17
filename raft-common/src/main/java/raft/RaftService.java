package raft;

import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RaftService {

	private RaftOptions raftOptions;

	private ServerRole role;
	//
	private ServerState state;
	// leader节点id
	private int leaderId;

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

	private synchronized void handleElectionTimeout() {

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
