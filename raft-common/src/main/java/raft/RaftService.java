package raft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.file.FileLogStore;
import raft.file.FileServerStateManager;
import raft.file.FileSnapshotState;

import java.util.*;
import java.util.concurrent.*;

public class RaftService {

    private static final Comparator<Long> indexComparator = new Comparator<Long>(){

        @Override
        public int compare(Long arg0, Long arg1) {
            return (int)(arg1.longValue() - arg0.longValue());
        }};

    private Logger logger;

    private FileLogStore logStore;

    private FileSnapshotState stateMachine;

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
    private int votesResponded;
    private boolean electionCompleted;
    private long quickCommitIndex;

    private PeerServer serverToJoin = null;

    private Random random;
    // 延迟任务
    private ScheduledFuture<?> scheduledElection;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private ScheduledExecutorService scheduledExecutorService;

    private Callable<Void> electionTimeoutTask;

    private boolean configChanging = false;

    private ClusterConfiguration config;

    private FileServerStateManager serverStateManager;

    public RaftService() {
        this.logger = LogManager.getLogger(getClass());
        //投票数
        this.votesGranted = 0;
        this.votesResponded = 0;
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

        if (this.state == null) {
            this.state = new ServerState();
            this.state.setTerm(0);
            this.state.setVotedFor(-1);
            this.state.setCommitIndex(0);
        }

        for (long i = Math.max(this.state.getCommitIndex() + 1, this.logStore.getStartIndex()); i < this.logStore.getFirstAvailableIndex(); ++i) {
            LogEntry logEntry = this.logStore.getLogEntryAt(i);
            if (logEntry.getVaueType() == LogValueType.Configuration) {
                this.logger.info("detect a configuration change that is not committed yet at index %d", i);
                this.configChanging = true;
                break;
            }
        }

        for (ClusterServer server : this.config.getServers()) {
            if (server.getId() != this.id) {
//                this.peers.put(server.getId(), new PeerServer(server))
            }
        }

        scheduledExecutorService = Executors.newScheduledThreadPool(2);

        //转换follower角色状态
        this.role = ServerRole.Follower;

        this.restartElectionTimer();
        this.logger.info("Server %d started", this.id);
    }


    /**
     * 处理服务端来的请求
     *
     * @param request
     * @return
     */
    public RaftResponseMessage processRequest(RaftRequestMessage request) {
        return null;
    }

    /**
     * 处理请求投票信息
     *
     * @param request
     * @return
     */
    private synchronized RaftResponseMessage handleVoteRequest(RaftRequestMessage request) {
        //如果请求任期号大于服务的任期号
        //更新服务的状态
        this.updateTerm(request.getTerm());


        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.RequestVoteResponse);
        response.setSource(this.id);
        response.setDestination(request.getSource());
        response.setTerm(this.state.getTerm());
        //如果请求日志条目任期号小于当前服务日志的任期号，要判断当前服务日志索引条目是不是小于等于请求的日志索引条目
        boolean logOkay = request.getLastLogTerm() > this.logStore.getLastLogEntry().getTerm() ||
                (request.getLastLogTerm() == this.logStore.getLastLogEntry().getTerm() &&
                        this.logStore.getFirstAvailableIndex() - 1 <= request.getLastLogIndex());
        //如果任期号相同，要判断日志条目
        boolean grant = request.getTerm() == this.state.getTerm() && logOkay &&
                (this.state.getVotedFor() == request.getSource() || this.state.getVotedFor() == -1);
        if (grant) {
            //更新领导者的ID
            this.state.setVotedFor(request.getSource());
            //保存状态
            this.serverStateManager.persistState(this.state);
        }
        return response;
    }

    private synchronized RaftResponseMessage handleAppendEntriesRequest(RaftRequestMessage request) {

        this.updateTerm(request.getTerm());
        if (request.getTerm() == this.state.getTerm()) {
            //
            if (this.role == ServerRole.Candidate) {
                this.becomeFollower();
            } else if (this.role == ServerRole.Leader) {
                this.logger.error("Receive AppendEntriesRequest from another leader(%d) with same term, there must be a bug, server exits", request.getSource());
                System.exit(-1);
            } else {
                this.restartElectionTimer();
            }
        }

        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.AppendEntriesResponse);
        response.setTerm(this.state.getTerm());
        response.setSource(this.id);
        response.setDestination(request.getSource());
        //如果请求的索引日志下标不为0,根据请求的日志索引找到当前服务日志的下标与任期号是否一致
        boolean logOkay = request.getLastLogIndex() == 0 ||
                (request.getLastLogIndex() < this.logStore.getFirstAvailableIndex() &&
                        request.getLastLogTerm() == this.termForLastLog(request.getLastLogIndex()));
        //如果请求的任期号大于当前服务的任期号，判断日志是否正确
        if (request.getTerm() < this.state.getTerm() || !logOkay) {
            response.setGranted(false);
            response.setNextIndex(this.logStore.getFirstAvailableIndex());
            return response;
        }
        //如果日志数据不为空
        if (request.getLogEntries() != null && request.getLogEntries().length > 0) {
            LogEntry[] logEntries = request.getLogEntries();
            long index = request.getLastLogIndex() + 1;
            int logIndex = 0;
            while (index < this.logStore.getFirstAvailableIndex() &&
                    logIndex < logEntries.length &&
                    logEntries[logIndex].getTerm() == this.logStore.getLogEntryAt(index).getTerm()) {
                logIndex++;
                index++;
            }

            while (index < this.logStore.getFirstAvailableIndex() && logIndex < logEntries.length) {
                LogEntry oldEntry = this.logStore.getLogEntryAt(index);
                if (oldEntry.getVaueType() == LogValueType.Application) {

                } else if (oldEntry.getVaueType() == LogValueType.Configuration) {

                }
                this.logStore.writeAt(index++, logEntries[logIndex++]);
            }

            while (logIndex < logEntries.length) {
                LogEntry logEntry = logEntries[logIndex++];

            }
        }

        this.leaderId = request.getSource();
        this.commit(request.getCommitIndex());
        response.setGranted(true);
        response.setNextIndex(request.getLastLogIndex() + (request.getLogEntries() == null ? 0 : request.getLogEntries().length) + 1);
        return response;

    }


    /**
     * 选举请求
     */
    void handleVoteRequest() {

    }

    //
    private synchronized void handleElectionTimeout() {


        //如果当前是领导人
        if (this.role == ServerRole.Leader) {
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

        for (PeerServer peer : this.peers.values()) {
            RaftRequestMessage request = new RaftRequestMessage();
            //请求投票标示
            request.setMessageType(RaftMessageType.RequestVoteRequest);
            //请求的服务ID
            request.setDestination(peer.getId());
            //
            request.setSource(this.id);
            //
            request.setLastLogIndex(this.logStore.getFirstAvailableIndex() - 1);

            request.setLastLogTerm(this.termForLastLog(this.logStore.getFirstAvailableIndex() - 1));
            //任期号
            request.setTerm(this.state.getTerm());
            this.logger.debug("send %s to server %d with term %d", RaftMessageType.RequestVoteRequest.toString(), peer.getId(), this.state.getTerm());
            peer.sendRequest(request).whenCompleteAsync((RaftResponseMessage response, Throwable error) -> {
                handlePeerResponse(response, error);
            });
        }
    }

    /**
     * 处理其他服务返回的结果响应
     *
     * @param response
     * @param error
     */
    private synchronized void handlePeerResponse(RaftResponseMessage response, Throwable error) {
        if (error != null) {
            this.logger.info("peer response error: %s", error.getMessage());
            return;
        }

        this.logger.debug(
                "Receive a %s message from peer %d with Result=%s, Term=%d, NextIndex=%d",
                response.getMessageType().toString(),
                response.getSource(),
                String.valueOf(response.isGranted()),
                response.getTerm(),
                response.getNextIndex());

        if (this.updateTerm(response.getTerm())) {
            return;
        }
        //如果返回的任期号小于当前任期号就直接返回不做后续处理
        if (response.getTerm() < this.state.getTerm()) {
            this.logger.info("Received a peer response from %d that with lower term value %d v.s. %d", response.getSource(), response.getTerm(), this.state.getTerm());
            return;
        }
        if (response.getMessageType() == RaftMessageType.RequestVoteResponse) {
            this.handleVotingResponse(response);
        } else if (response.getMessageType() == RaftMessageType.AppendEntriesResponse) {
            this.handleAppendEntriesResponse(response);
        } else {
            this.logger.error("Received an unexpected message %s for response, system exits.", response.getMessageType().toString());
            System.exit(-1);
        }
    }

    private void handleAppendEntriesResponse(RaftResponseMessage response) {
        PeerServer peer = this.peers.get(response.getSource());
        if (peer == null) {
            this.logger.info("the response is from an unkonw peer %d", response.getSource());
            return;
        }

        if(response.isGranted()){
            synchronized (peers){
                peer.setNextLogIndex(response.getNextIndex());
                peer.setMatchedIndex(response.getNextIndex()-1);
            }
            ArrayList<Long> matchedIndexes = new ArrayList<>(this.peers.size()+1);
            matchedIndexes.add(this.logStore.getFirstAvailableIndex()-1);
            for(PeerServer p : this.peers.values()){
                matchedIndexes.add(p.getMatchedIndex());
            }
            matchedIndexes.sort(indexComparator);


        }else{
            synchronized (peers){

            }
        }
    }

    private void handleVotingResponse(RaftResponseMessage response) {
        //累加返回数
        this.votesResponded += 1;
        if (this.electionCompleted) {
            this.logger.info("Election completed, will ignore the voting result from this server");
            return;
        }
        //累加同意的选票数
        if (response.isGranted()) {
            this.votesGranted += 1;
        }

        if (this.votesResponded >= this.peers.size() + 1) {
            this.electionCompleted = true;
        }

        if (this.votesGranted > (this.peers.size() + 1) / 2) {
            this.logger.info("Server is elected as leader for term %d", this.state.getTerm());
            this.electionCompleted = true;
            //开始执行leader状态转变
            this.becomeLeader();
        }
    }

    private boolean updateTerm(long term) {
        if (term > this.state.getTerm()) {
            this.state.setTerm(term);
            this.state.setVotedFor(-1);
            this.electionCompleted = false;
            this.votesGranted = 0;
            this.votesResponded = 0;
            //TODO 保存状态
            this.serverStateManager.persistState(this.state);
            this.becomeFollower();
            return true;
        }
        return false;
    }

    /**
     * 执行follower
     */
    private void becomeFollower() {
        //停掉心跳任务
        for (PeerServer server : this.peers.values()) {
            if (server.getHeartbeatTask() != null) {
                server.getHeartbeatTask().cancel(false);
            }
            server.enableHeartbeat(false);
        }
        this.serverToJoin = null;
        this.role = ServerRole.Follower;
        //开启选举定时任务
        this.restartElectionTimer();
    }

    private void becomeLeader() {
        //先停止选举定时
        this.stopElectionTimer();
        //转换为领导者
        this.role = ServerRole.Leader;
        //设置领导者ID
        this.leaderId = this.id;
        //领导者服务类赋值null
        this.serverToJoin = null;

        //
        for (PeerServer peerServer : this.peers.values()) {
            //设置下一个可用下标
            peerServer.setNextLogIndex(this.logStore.getFirstAvailableIndex());

            peerServer.setFree();
            //TODO 待补充
            //开启心跳
            this.enableHeartbeatForPeer(peerServer);
        }

        //TODO 待补充

        this.requestAppendEntries();

    }

    private void requestAppendEntries() {

        if (this.peers.size() == 0) {
            this.commit(this.logStore.getFirstAvailableIndex() - 1);
            //TODO 待补充
            return;
        }

        for (PeerServer peer : this.peers.values()) {
            this.requestAppendEntries(peer);
        }


    }

    private void commit(long targetIndex) {
        if (targetIndex > this.quickCommitIndex) {
            this.quickCommitIndex = targetIndex;
            //如果是leader要通知peers
            if (this.role == ServerRole.Leader) {
                for (PeerServer peer : this.peers.values()) {

                    if (!this.requestAppendEntries(peer)) {
                        peer.setPendingCommit();
                    }
                }
            }
        }
        if (this.logStore.getFirstAvailableIndex() - 1 > this.state.getCommitIndex() && this.quickCommitIndex > this.state.getCommitIndex()) {
            //TODO 这里要进行快照的变更提交
        }
    }

    private boolean requestAppendEntries(PeerServer peer) {
        //如果不空闲就开始发送
        if (peer.makeBusy()) {
            //开始发送
            peer.sendRequest(this.createAppendEntriesRequest(peer))
                    .whenCompleteAsync((RaftResponseMessage response, Throwable error) -> {
                        try {
                            handlePeerResponse(response, error);
                        } catch (Throwable err) {
                            this.logger.error("Uncaught exception %s", err.toString());
                        }
                    }, this.scheduledExecutor)
            ;
            return true;
        }
        return false;
    }

    //创建日志请求，如果当前服务的日志
    private RaftRequestMessage createAppendEntriesRequest(PeerServer peer) {
        long currentNextIndex = 0;
        long commitIndex = 0;
        long lastLogIndex = 0;
        long term = 0;
        long startingIndex = 1;
        synchronized (this) {
            //当前服务日志开始的下标
            startingIndex = this.logStore.getStartIndex();
            //当前服务日志尾下标
            currentNextIndex = this.logStore.getFirstAvailableIndex();
            //当前服务提交日志的下标
            commitIndex = this.quickCommitIndex;
            //当前服务任期号
            term = this.state.getTerm();
        }
        synchronized (peer) {
            //如果其他服务的尾下标为0就设置当前服务尾下标
            if (peer.getNextLogIndex() == 0) {
                peer.setNextLogIndex(currentNextIndex);
            }
            //其他服务日志提交尾下标
            lastLogIndex = peer.getNextLogIndex() - 1;
        }
        //如果其他服务日志尾下标大于当前服务日志的尾下标，退出服务
        if (lastLogIndex >= currentNextIndex) {
            this.logger.error("Peer's lastLogIndex is too large %d v.s. %d, server exits", lastLogIndex, currentNextIndex);
            System.exit(-1);
        }
        //如果其他服务的日志尾小于当前服务日志开始下标，要进行快照
        if (lastLogIndex > 0 && lastLogIndex < startingIndex - 1) {
            return this.createSyncSnapshotRequest(peer, lastLogIndex, term, commitIndex);
        }
        //获取当前服务日志尾下标的任期号
        long lastLogTerm = this.termForLastLog(lastLogIndex);

        long endIndex = Math.min(currentNextIndex, lastLogIndex + 1 + raftOptions.getMaximumAppendingSize());
        //如果其他服务尾日志小于当前的尾日志，就获取当前的服务的所有日志记录
        LogEntry[] logEntries = (lastLogIndex + 1) >= endIndex ? null :
                this.logStore.getLogEntries(lastLogIndex + 1, endIndex);

        RaftRequestMessage requestMessage = new RaftRequestMessage();
        requestMessage.setMessageType(RaftMessageType.AppendEntriesRequest);
        requestMessage.setSource(this.id);
        requestMessage.setDestination(peer.getId());
        requestMessage.setLastLogIndex(lastLogIndex);
        requestMessage.setLastLogTerm(lastLogTerm);

        requestMessage.setLogEntries(logEntries);
        requestMessage.setCommitIndex(commitIndex);
        requestMessage.setTerm(term);
        return requestMessage;


    }

    //TODO 快照日志还没有做
    private RaftRequestMessage createSyncSnapshotRequest(PeerServer peer, long lastLogIndex, long term, long commitIndex) {

        return null;
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

    /**
     * @param logIndex
     * @return
     */
    private long termForLastLog(long logIndex) {
        if (logIndex == 0) {
            return 0;
        }
        if (logIndex >= this.logStore.getStartIndex()) {
            return this.logStore.getLogEntryAt(logIndex).getTerm();
        }

        Snapshot lastSnapshot = this.stateMachine.getLastSnapshot();
        if (lastSnapshot == null || logIndex != lastSnapshot.getLastLogIndex()) {
            throw new IllegalArgumentException("logIndex is beyond the range that no term could be retrieved");
        }
        return lastSnapshot.getLastLogTerm();
    }
}
