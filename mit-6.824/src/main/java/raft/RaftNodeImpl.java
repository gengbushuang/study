package raft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.message.request.AppendEntriesRequest;
import raft.message.request.RequestVoteRequest;
import raft.message.response.AppendEntriesResponse;
import raft.message.response.RequestVoteResponse;
import raft.t.ServerRole;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class RaftNodeImpl implements RaftNode {

    private Logger logger = LogManager.getLogger(getClass());

    private RaftContext context;

    private Random random = new Random();

    private int serviceId;

    private int leaderId;

    private ServerRole role;

    private RaftState state;

    private ScheduledFuture<?> scheduled;

    private Map<Integer, RaftNodeClient> nodeClientMap = new HashMap<>();

    public RaftNodeImpl(RaftContext context) {
        this.context = context;


        this.role = ServerRole.Follower;
    }

    @Override
    public long getTerm() {
        return state.getTerm();
    }

    @Override
    public void setLeaderId(int id) {
        this.leaderId = id;
    }

    @Override
    public int getServiceId() {
        return serviceId;
    }

    @Override
    public RaftState getState() {
        return state;
    }

    @Override
    public ServerRole getRole() {
        return role;
    }

    @Override
    public void updateTerm(long term) {
        if (term > getTerm()) {
            state.setTerm(term);
            this.updateVoteFor(-1);
            this.votesCount = 0;
            this.becomeFollower();
        }
    }

    @Override
    public void updateVoteFor(long serverId) {
        state.setVotedFor(serverId);
    }

    @Override
    public void resetElection() {
        this.restartElectionTimer();
    }

    @Override
    public void becomeFollower() {
        //如果上次是Leader角色，停止Leader心跳定时任务
        if (this.role == ServerRole.Leader) {
            this.stopElectionTimer();
        }
        //
        this.role = ServerRole.Follower;
        logger.info("node convert role {}", this.role);
        //重新设置选举定时任务
        this.restartElectionTimer();
    }

    /**
     * 启动定时选举
     */
    private void restartElectionTimer() {
        if (this.scheduled != null) {
            this.scheduled.cancel(false);
        }

        RaftOptions raftOptions = this.context.getRaftOptions();
        //计算时间上界和下界的差值
        int lowerUpperSection = raftOptions.getElectionTimeoutUpperBound() - raftOptions.getElectionTimeoutLowerBound();
        //计算定时任务时间
        int electionTime = raftOptions.getElectionTimeoutLowerBound() + random.nextInt(lowerUpperSection + 1);
        //定时选举
        this.scheduled = this.context.getScheduledExecutor().schedule(() -> this.electionTimeoutTask(), electionTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止定时选举
     */
    private void stopElectionTimer() {
        if (this.scheduled == null) {
            return;
        }
        this.scheduled.cancel(false);
        this.scheduled = null;
    }

    //选举定时task
    private synchronized void electionTimeoutTask() {
        if (this.role == ServerRole.Leader) {
            System.exit(-1);
            return;
        }

        this.role = ServerRole.Candidate;
        logger.info("node convert role {}", this.role);
        //投票之前先重置投票数
        this.votesCount = 0;
        this.state.setTerm(this.state.getTerm() + 1);
        //开始进行投票
        this.VoteRequest();

        if (this.role != ServerRole.Leader) {
            this.restartElectionTimer();
        }
    }

    private void VoteRequest() {
        //先加上自己的投票数
        this.votesCount += 1;

        final RequestVoteRequest voteRequest = new RequestVoteRequest();
        voteRequest.setTerm(this.state.getTerm());

        for (RaftNodeClient nodeClient : nodeClientMap.values()) {
            nodeClient.SendRequest(proxy -> {
                try {
                    RequestVoteResponse voteResponse = proxy.sendVote(voteRequest);
                    this.logger.info("Response back a {}", voteResponse);
                    processVoteResponse(voteResponse);
                } catch (Exception e) {

                }
            });
        }

    }

    private int votesCount = 0;

    private synchronized void processVoteResponse(RequestVoteResponse voteResponse) {
        //如果返回的term比自己的大，就转换为Follower角色
        if (voteResponse.getTerm() > state.getTerm()) {
            this.becomeFollower();
            return;
        }
        //如果自己不是Candidate角色，则返回
        if (role != ServerRole.Candidate) {
            return;
        }
        //如果返回的term比自己小，则返回
        if (voteResponse.getTerm() < state.getTerm()) {
            return;
        }
        //对给自己投票的进行累加
        if (voteResponse.isGranted()) {
            this.votesCount += 1;
        }
        //如果投票数超过半数就转换Leader角色
        if (this.votesCount > (nodeClientMap.values().size() + 1) / 2) {
            this.becomeLeader();
        }

    }


    @Override
    public void becomeLeader() {
        //取消选举定时任务
        this.stopElectionTimer();

        this.role = ServerRole.Leader;
        logger.info("node convert role {}", this.role);

        //启动定时任务
        this.restartHeartbeatTimer();

    }

    @Override
    public AppendEntriesResponse handleAppendEntriesRequest(AppendEntriesRequest appendEntriesRequest) {
        AppendEntriesResponse entriesResponse = new AppendEntriesResponse();
        entriesResponse.setTerm(this.state.getTerm());
        entriesResponse.setSuccess(true);
        return entriesResponse;
    }

    private void restartHeartbeatTimer() {
        if (this.scheduled != null) {
            this.scheduled.cancel(false);
        }
        RaftOptions raftOptions = this.context.getRaftOptions();

        this.scheduled = this.context.getScheduledExecutor().schedule(() -> this.enableHeartbeatTimeoutTask(), raftOptions.getHeartbeatInterval(), TimeUnit.MILLISECONDS);
    }

    private synchronized void enableHeartbeatTimeoutTask() {
        if (this.role == ServerRole.Leader) {

            this.entriesRequest();

            this.restartHeartbeatTimer();
        }
    }

    private void entriesRequest() {
        final AppendEntriesRequest entriesRequest = new AppendEntriesRequest();
        entriesRequest.setTerm(this.state.getTerm());
        entriesRequest.setSource(this.serviceId);
        for (RaftNodeClient nodeClient : nodeClientMap.values()) {
            entriesRequest.setDestination(nodeClient.getServiceId());
            nodeClient.SendRequest(proxy -> {
                AppendEntriesResponse entriesResponse = proxy.sendEntrie(entriesRequest);
                this.logger.info("Response back a {}", entriesResponse);
                processEntriesResponse(entriesResponse);
            });
        }
    }

    private synchronized void processEntriesResponse(AppendEntriesResponse entriesResponse) {
        //返回的term比自己大，转化为Follower角色
        if (entriesResponse.getTerm() > this.state.getTerm()) {
            this.updateTerm(entriesResponse.getTerm());
            return;
        }

        //
        if (this.role != ServerRole.Leader) {
            logger.warn("Receive AppendEntriesResponse role is not leader {}", entriesResponse.getSource());
        }
    }
}
