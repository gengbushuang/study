package org.raft;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raft.*;
import raft.file.FileLogStore;
import raft.file.FileStateMachine;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gbs on 19/4/4.
 */
public class NewRaftServer implements RaftMessageHandler {

    private Logger logger;

    private static final Comparator<Long> indexComparator = new Comparator<Long>() {

        @Override
        public int compare(Long arg0, Long arg1) {
            return (int) (arg1.longValue() - arg0.longValue());
        }
    };

    //当前服务ID标示
    private int sid;
    //领导者ID
    private int leaderId;
    //服务状态
    private ServerState state;
    //服务角色
    private ServerRole serverRole;
    //记录赞成投票数
    private int votesGranted;
    //记录返回的数
    private int votesResponded;

    private boolean electionCompleted;

    private long quickCommitIndex;

    private AtomicInteger snapshotInProgress;

    private NewRaftContext raftContext;
    private RaftOptions raftOptions;
    //选举
    private ScheduledFuture<?> scheduled;
    //选举任务
    private Callable<Void> electionTimeoutTask;

    private Random random;

    private ClusterConfiguration conf;
    private Map<Integer, NewPeerServer> peerServers = new HashMap<>();
    //日志
    private FileLogStore logStore;
    private FileStateMachine stateMachine;

    private CommittingThread commitingThread;


    public NewRaftServer(int sid, NewRaftContext raftContext) {
        this.logger = LogManager.getLogger(getClass());
        this.logStore = raftContext.getServerStateManager().loadLogStore();
        this.conf = raftContext.getServerStateManager().loadClusterConfiguration();
        this.stateMachine = raftContext.getStateMachine();
        this.state = raftContext.getServerStateManager().readState();


        this.raftContext = raftContext;
        this.raftOptions = this.raftContext.getRaftOptions();
        this.raftContext.getScheduledExecutor();


        this.sid = sid;
        this.leaderId = -1;
        this.votesGranted = 0;
        this.votesResponded = 0;

        //随机类
        this.random = new Random(Calendar.getInstance().getTimeInMillis());

        if (this.state == null) {
            this.state = new ServerState();
            this.state.setCommitIndex(0);
            this.state.setTerm(0);
            this.state.setVotedFor(-1);
        }
        //设置选举定时任务
        this.electionTimeoutTask = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                handleElectionTimeout();
                return null;
            }
        };
        //
        for (ClusterServer server : this.conf.getServers()) {
            if (this.sid != server.getId()) {
                peerServers.put(server.getId(), new NewPeerServer(server, raftContext, peerServer -> this.handleHeartbeatTimeout(peerServer)));
            }
        }
        this.snapshotInProgress = new AtomicInteger(0);
        this.electionCompleted = false;
        this.quickCommitIndex = this.state.getCommitIndex();
        this.commitingThread = new CommittingThread(this);
        //初始化服务角色
        this.serverRole = ServerRole.Follower;
        new Thread(this.commitingThread).start();
        //启动重新选举任务
        this.restartElectionTimer();
        this.logger.info("Server {} started", this.sid);
    }

    private synchronized void handleHeartbeatTimeout(NewPeerServer peer) {
        this.logger.info("Heartbeat timeout for {}", peer.getId());
        if (this.serverRole == ServerRole.Leader) {
            this.requestAppendEntries(peer);

            synchronized (peer) {
                if (peer.isHeartbeatEnabled()) {
                    // Schedule another heartbeat if heartbeat is still enabled
                    peer.setHeartbeatTask(this.raftContext.getScheduledExecutor().schedule(peer.getHeartbeartHandler(), peer.getCurrentHeartbeatInterval(), TimeUnit.MILLISECONDS));
                } else {
                    this.logger.info("heartbeat is disabled for peer {}", peer.getId());
                }
            }
        } else {
            this.logger.info("Receive a heartbeat event for {} while no longer as a leader", peer.getId());
        }
    }

    /**
     * 启动定时选举
     */
    private void restartElectionTimer() {
        if (this.scheduled != null) {
            this.scheduled.cancel(false);
        }
        //计算时间上界和下界的差值
        int lowerUpperSection = this.raftOptions.getElectionTimeoutUpperBound() - this.raftOptions.getElectionTimeoutLowerBound();
        //计算定时任务时间
        int electionTime = this.raftOptions.getElectionTimeoutLowerBound() + random.nextInt(lowerUpperSection + 1);
        this.logger.info("[restartElectionTimer][sid={}][time={}]", this.sid, electionTime);
        //设置定时选举
        this.scheduled = this.raftContext.getScheduledExecutor().schedule(this.electionTimeoutTask, electionTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止定时选举
     */
    private void stopElectionTimer() {
        this.logger.info("[stopElectionTimer][sid={}]", this.sid);
        if (this.scheduled == null) {
            return;
        }
        this.scheduled.cancel(false);
        this.scheduled = null;
    }

    /**
     * 选举处理
     */
    private synchronized void handleElectionTimeout() {
        this.logger.info("[start][handleElectionTimeout][role={}][sid={}]", this.serverRole, this.sid);
        this.logger.info("开始投票sid=" + this.sid);

        if (this.serverRole == ServerRole.Leader) {
            this.logger.error("A leader should never encounter election timeout, illegal application state, stop the application");
            System.exit(-1);
            return;
        }
        this.logger.info("Election timeout, change to Candidate");
        //累加任期号
        this.state.increaseTerm();
        //
        this.state.setVotedFor(-1);
        //角色变换Candidate，进行投票
        this.serverRole = ServerRole.Candidate;
        //
        this.votesResponded = 0;
        this.votesGranted = 0;
        this.electionCompleted = false;

        this.requestVote();
        this.logger.info("[end][handleElectionTimeout][role={}][sid={}]", this.serverRole, this.sid);
        if (this.serverRole != ServerRole.Leader) {
            this.restartElectionTimer();
        }
    }

    /**
     * 投票请求处理
     */
    private void requestVote() {
        this.logger.info("requestVote started with term {}", this.state.getTerm());
        //先记录自己的投票数
        this.votesGranted += 1;
        this.votesResponded += 1;
        this.state.setVotedFor(this.sid);
        this.raftContext.getServerStateManager().persistState(this.state);
        if (this.votesGranted > (this.peerServers.size() + 1) / 2) {
            this.electionCompleted = true;
            this.becomeLeader();
            return;
        }

        for (NewPeerServer peer : this.peerServers.values()) {
            RaftRequestMessage requestMessage = new RaftRequestMessage();
            requestMessage.setMessageType(RaftMessageType.RequestVoteRequest);
            requestMessage.setSource(this.sid);
            requestMessage.setDestination(peer.getId());
            requestMessage.setTerm(this.state.getTerm());
            requestMessage.setLastLogIndex(this.logStore.getFirstAvailableIndex() - 1);
            requestMessage.setLastLogTerm(this.termForLastLog(this.logStore.getFirstAvailableIndex() - 1));

            this.logger.debug("send {} to server {} with term {}", RaftMessageType.RequestVoteRequest.toString(), peer.getId(), this.state.getTerm());

            peer.sendRequest(requestMessage).whenCompleteAsync((RaftResponseMessage response, Throwable err) -> {
                processResponse(response, err);
            }, this.raftContext.getScheduledExecutor());
        }

    }

    /**
     * 响应结果处理
     *
     * @param response
     * @param error
     */
    private synchronized void processResponse(RaftResponseMessage response, Throwable error) {
        if (error != null) {
            this.logger.info("peer response error: {}", error.getMessage());
            return;
        }

        this.logger.info(
                "Receive a {} message from peer {} with Result={}, Term={}, NextIndex={}",
                response.getMessageType().toString(),
                response.getSource(),
                String.valueOf(response.isGranted()),
                response.getTerm(),
                response.getNextIndex());

        if (this.updateTerm(response.getTerm())) {
            return;
        }

        //返回的任期号小于当前服务的任期号返回不作任何操作
        if (response.getTerm() < this.state.getTerm()) {
            this.logger.info("Received a peer response from {} that with lower term value {} v.s. {}", response.getSource(), response.getTerm(), this.state.getTerm());
            return;
        }

        if (response.getMessageType() == RaftMessageType.RequestVoteResponse) {
            this.handleVoteResponse(response);
        } else if (response.getMessageType() == RaftMessageType.AppendEntriesResponse) {
            this.handleAppendEntriesResponse(response);
        } else {
            this.logger.error("Received an unexpected message {} for response, system exits.", response.getMessageType().toString());
            System.exit(-1);
        }


    }


    private void handleVoteResponse(RaftResponseMessage response) {
        //记录响应的数
        this.votesResponded += 1;
        if (this.electionCompleted) {
            this.logger.info("Election completed, will ignore the voting result from this server");
            return;
        }

        if (response.isGranted()) {
            //记录赞成的投票数
            this.votesGranted += 1;
        }

        if (this.votesResponded >= this.peerServers.size() + 1) {
            this.electionCompleted = true;
        }

        //如果赞成数超过半数，切换状态为Leader
        if (this.votesGranted > (this.peerServers.size() + 1) / 2) {
            this.logger.info("Server is elected as leader for term {}", this.state.getTerm());
            this.electionCompleted = true;
            this.becomeLeader();
        }
    }

    private void handleAppendEntriesResponse(RaftResponseMessage response) {
        NewPeerServer peerServer = this.peerServers.get(response.getSource());
        if (peerServer == null) {
            this.logger.info("the response is from an unkonw peer {}", response.getSource());
            return;
        }

        boolean needToCatchup = true;
        if (response.isGranted()) {
            synchronized (peerServer) {
                peerServer.setNextLogIndex(response.getNextIndex());
                //每个追随者同步正确的最后日志索引
                peerServer.setMatchedIndex(response.getNextIndex() - 1);
            }
            //记录要提交的日志索引
            ArrayList<Long> matchedIndexes = new ArrayList<>(this.peerServers.size() + 1);
            matchedIndexes.add(this.logStore.getFirstAvailableIndex() - 1);
            for (NewPeerServer peer : this.peerServers.values()) {
                matchedIndexes.add(peer.getMatchedIndex());
            }
            //领导者提交后，在进行追随者提交
            matchedIndexes.sort(indexComparator);
            this.commit(matchedIndexes.get((this.peerServers.size() + 1) / 2));
            needToCatchup = peerServer.clearPendingCommit() || response.getNextIndex() < this.logStore.getFirstAvailableIndex();
        } else {
            //领导者维护的追随者日志索引大于追随者自身的日志索引，必须重新设置维护追随者日志索引
            synchronized (peerServer) {
                if (response.getNextIndex() > 0 && response.getNextIndex() < peerServer.getNextLogIndex()) {
                    peerServer.setNextLogIndex(response.getNextIndex());
                } else {
                    //往前找寻索引正确位置
                    peerServer.setNextLogIndex(peerServer.getNextLogIndex() - 1);
                }
            }
        }
        //如果
        if (this.serverRole == ServerRole.Leader && needToCatchup) {
            this.requestAppendEntries(peerServer);
        }

    }


    private boolean updateTerm(long term) {
        //如果接收到的 RPC 请求或响应中，任期号T > currentTerm，那么就令 currentTerm 等于 T，并切换状态为跟随者
        if (term > this.state.getTerm()) {
            this.state.setTerm(term);
            this.state.setVotedFor(-1);
            this.votesGranted = 0;
            this.votesResponded = 0;
            this.electionCompleted = false;
            this.raftContext.getServerStateManager().persistState(this.state);
            this.becomeFollower();
            return true;
        }
        return false;
    }

    /**
     * leader的变换
     */
    private void becomeLeader() {
        this.logger.info("[becomeLeader][sid={}]", this.sid);
        //停止选举任务
        this.stopElectionTimer();
        //设置领导者ID
        this.leaderId = this.sid;
        //角色变换为leader
        this.serverRole = ServerRole.Leader;
        //
        for (NewPeerServer peer : this.peerServers.values()) {
            this.logger.info("start heartbeat sid {}", peer.getId());
            peer.setNextLogIndex(this.logStore.getFirstAvailableIndex());
            //开启心跳
            peer.setFree();
            this.enableHeartbeatForPeer(peer);
        }


        this.requestAppendEntries();
    }

    private void enableHeartbeatForPeer(NewPeerServer peer) {
        this.logger.info("[start][enableHeartbeatForPeer][sid={}][time={}]", this.sid, peer.getCurrentHeartbeatInterval());
        peer.enableHeartbeat(true);
        peer.resumeHeartbeatingSpeed();
        peer.setHeartbeatTask(this.raftContext.getScheduledExecutor().schedule(peer.getHeartbeartHandler(), peer.getCurrentHeartbeatInterval(), TimeUnit.MILLISECONDS));
        this.logger.info("[end][enableHeartbeatForPeer][sid={}][time={}]", this.sid, peer.getCurrentHeartbeatInterval());
    }

    /**
     * Followe的变换
     */
    private void becomeFollower() {
        //停止心跳
        for (NewPeerServer peer : this.peerServers.values()) {
            this.logger.info("stop heartbeat sid {}", peer.getId());
            if (peer.getHeartbeatTask() != null) {
                peer.getHeartbeatTask().cancel(false);
            }

            peer.enableHeartbeat(false);
        }

        this.serverRole = ServerRole.Follower;
        //启动重新选举任务
        this.restartElectionTimer();
    }

    /**
     * 提交索引
     *
     * @param targetIndex
     */
    private void commit(long targetIndex) {
        if (targetIndex > this.quickCommitIndex) {
            this.quickCommitIndex = targetIndex;

            if (this.serverRole == ServerRole.Leader) {
                for (NewPeerServer peer : this.peerServers.values()) {
                    //如果上次发送还没有好，就记录没有提交成功标记
                    if (!this.requestAppendEntries(peer)) {
                        peer.setPendingCommit();
                    }
                }
            }
        }

        if (this.state.getCommitIndex() < this.quickCommitIndex && this.logStore.getFirstAvailableIndex() - 1 > this.state.getCommitIndex()) {
            this.commitingThread.moreToCommit();
        }

    }


    /**
     *
     */
    private void requestAppendEntries() {
        if (this.peerServers.size() == 0) {
            this.commit(this.logStore.getFirstAvailableIndex() - 1);
            return;
        }

        for (NewPeerServer peer : this.peerServers.values()) {
            this.requestAppendEntries(peer);
        }
    }

    /**
     * @param peer
     * @return
     */
    private boolean requestAppendEntries(NewPeerServer peer) {
        if (peer.makeBusy()) {
            peer.sendRequest(this.createAppendEntriesRequest(peer))
                    .whenCompleteAsync((RaftResponseMessage response, Throwable err) -> {
                        try {
                            processResponse(response, err);
                        } catch (Throwable e) {
                            this.logger.error("Uncaught exception {}", err.toString());
                        }
                    }, this.raftContext.getScheduledExecutor());
            return true;
        }
        return false;
    }

    /**
     * @param peer
     * @return
     */
    private RaftRequestMessage createAppendEntriesRequest(NewPeerServer peer) {
        long currentNextIndex = 0;
        long startingIndex = 1;
        long lastLogIndex = 0;
        long term = 0;
        long commitIndex = 0;
        synchronized (this) {
            //领导者起始索引
            startingIndex = this.logStore.getStartIndex();
            term = this.state.getTerm();
            //领导者最后索引值加一
            currentNextIndex = this.logStore.getFirstAvailableIndex();
            commitIndex = this.quickCommitIndex;
        }

        synchronized (peer) {
            if (peer.getNextLogIndex() == 0) {
                peer.setNextLogIndex(currentNextIndex);
            }
            //追随者日志最后索引
            lastLogIndex = peer.getNextLogIndex() - 1;
        }

        if (lastLogIndex >= currentNextIndex) {
            this.logger.error("Peer's lastLogIndex is too large {} v.s. {}, server exits", lastLogIndex, currentNextIndex);
            System.exit(-1);
        }


        //比较领导日志最后索引和追随者日志最后索引
        long endIndex = Math.min(currentNextIndex, lastLogIndex + 1 + this.raftOptions.getMaximumAppendingSize());
        //复制领导者日志给追随者
        LogEntry[] logEntries = lastLogIndex + 1 >= endIndex ? null : this.logStore.getLogEntries(lastLogIndex + 1, endIndex);

        long lastLogTerm = this.termForLastLog(lastLogIndex);

        RaftRequestMessage requestMessage = new RaftRequestMessage();
        requestMessage.setMessageType(RaftMessageType.AppendEntriesRequest);
        requestMessage.setSource(this.sid);
        requestMessage.setDestination(peer.getId());
        requestMessage.setLastLogIndex(lastLogIndex);
        requestMessage.setLastLogTerm(lastLogTerm);
        requestMessage.setLogEntries(logEntries);
        requestMessage.setCommitIndex(commitIndex);
        requestMessage.setTerm(term);

        return requestMessage;
    }


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


    @Override
    public RaftResponseMessage processRequest(RaftRequestMessage request) {

        this.logger.info(
                "Receive a {} message from {} with LastLogIndex={}, LastLogTerm={}, EntriesLength={}, CommitIndex={} and Term={}",
                request.getMessageType().toString(),
                request.getSource(),
                request.getLastLogIndex(),
                request.getLastLogTerm(),
                request.getLogEntries() == null ? 0 : request.getLogEntries().length,
                request.getCommitIndex(),
                request.getTerm());

        RaftResponseMessage response = null;

        if (request.getMessageType() == RaftMessageType.RequestVoteRequest) {
            response = this.handleVoteRequest(request);
        } else if (request.getMessageType() == RaftMessageType.AppendEntriesRequest) {
            response = this.handleAppendEntriesRequest(request);
        } else {
            response = this.handleExtendedMessages(request);
        }


        if (response != null) {
            this.logger.info(
                    "Response back a {} message to {} with Accepted={}, Term={}, NextIndex={}",
                    response.getMessageType().toString(),
                    response.getDestination(),
                    String.valueOf(response.isGranted()),
                    response.getTerm(),
                    response.getNextIndex());
        }

        return response;
    }


    private RaftResponseMessage handleVoteRequest(RaftRequestMessage request) {

        this.updateTerm(request.getTerm());
        //如果 votedFor 为空或者为 candidateId，并且候选人的日志至少和自己一样新，那么就投票给他
        //请求日志记录比当前的日志记录新就true
        boolean logOkay = request.getLastLogTerm() > this.logStore.getLastLogEntry().getTerm() ||
                (request.getLastLogTerm() == this.logStore.getLastLogEntry().getTerm() &&
                        request.getLastLogIndex() >= this.logStore.getFirstAvailableIndex() - 1);

        boolean grant = request.getTerm() == this.state.getTerm() &&
                logOkay &&
                (request.getSource() == this.state.getVotedFor() || this.state.getVotedFor() == -1);


        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.RequestVoteResponse);
        response.setSource(this.sid);
        response.setDestination(request.getSource());
        response.setTerm(this.state.getTerm());

        response.setGranted(grant);
        if (grant) {
            this.state.setVotedFor(request.getSource());
            this.raftContext.getServerStateManager().persistState(this.state);
        }

        return response;

    }

    private synchronized RaftResponseMessage handleAppendEntriesRequest(RaftRequestMessage request) {
        this.updateTerm(request.getTerm());

        if (request.getTerm() == this.state.getTerm()) {
            if (this.serverRole == ServerRole.Candidate) {
                this.becomeFollower();
            } else if (this.serverRole == ServerRole.Leader) {
                this.logger.error("Receive AppendEntriesRequest from another leader({}) with same term, there must be a bug, server exits", request.getSource());
                System.exit(-1);
            } else {
                this.restartElectionTimer();
            }
        }

        RaftResponseMessage response = new RaftResponseMessage();
        response.setMessageType(RaftMessageType.AppendEntriesResponse);
        response.setTerm(this.state.getTerm());
        response.setDestination(request.getSource());
        response.setSource(this.sid);
        // 如果领导者日志的最后索引小于追随者日志最后索引
        // 追随者日志里面有领导者日志的最后任期号
        boolean logOkay = request.getLastLogIndex() == 0 ||
                (request.getLastLogIndex() < this.logStore.getFirstAvailableIndex() &&
                        request.getLastLogTerm() == this.termForLastLog(request.getLastLogIndex()));
//        if(this.logStore.getFirstAvailableIndex()-1<response.getNextIndex())
        //如果 term < currentTerm 就返回 false
        if (request.getTerm() < this.state.getTerm() || !logOkay) {
            response.setGranted(false);
            response.setNextIndex(this.logStore.getFirstAvailableIndex());
            return response;
        }
        //领导者日志复制
        LogEntry[] logEntries = request.getLogEntries();
        if (logEntries != null && logEntries.length > 0) {
            long index = request.getLastLogIndex() + 1;
            int logIndex = 0;
            while (index < this.logStore.getFirstAvailableIndex() &&
                    logIndex < logEntries.length &&
                    logEntries[logIndex].getTerm() == this.logStore.getLogEntryAt(index).getTerm()) {
                index++;
                logIndex++;
            }
            while (index < this.logStore.getFirstAvailableIndex() && logIndex < logEntries.length) {
                LogEntry oldEntry = this.logStore.getLogEntryAt(index);

                this.logStore.writeAt(index, oldEntry);
                index++;
                logIndex++;
            }
            while (logIndex < logEntries.length) {
                this.logStore.append(logEntries[logIndex]);
                logIndex++;
            }
        }

        this.commit(request.getCommitIndex());
        this.leaderId = request.getSource();
        response.setGranted(true);
        response.setNextIndex(request.getLastLogIndex() + (logEntries == null ? 0 : logEntries.length) + 1);
        return response;
    }


    private RaftResponseMessage handleExtendedMessages(RaftRequestMessage request) {
        return null;
    }


    private void snapshotAndCompact(long indexCommitted){

        if(this.raftOptions.getSnapshotDistance()>0&&
                ((indexCommitted-this.logStore.getStartIndex())>this.raftOptions.getSnapshotDistance())
                ){
            return;
        }


    }

    static class CommittingThread implements Runnable {
        private NewRaftServer server;
        private Object conditionalLock;

        public CommittingThread(NewRaftServer server) {
            this.server = server;
            this.conditionalLock = new Object();
        }

        void moreToCommit(){
            synchronized(this.conditionalLock){
                this.conditionalLock.notify();
            }
        }

        @Override
        public void run() {
            try {
                long commitIndex = server.state.getCommitIndex();
                //状态
                while (server.quickCommitIndex <= commitIndex ||
                        commitIndex >= server.logStore.getFirstAvailableIndex() - 1) {
                    synchronized (this.conditionalLock) {
                        this.conditionalLock.wait();
                    }
                    commitIndex = server.state.getCommitIndex();
                }
                
                while (commitIndex<server.quickCommitIndex && commitIndex<server.logStore.getFirstAvailableIndex() - 1){
                    commitIndex+=1;
                    LogEntry logEntry = server.logStore.getLogEntryAt(commitIndex);

                    server.state.setCommitIndex(commitIndex);
                    server.snapshotAndCompact(commitIndex);
                }

                server.raftContext.getServerStateManager().persistState(server.state);
                
            } catch (Throwable error) {
                server.logger.error("error {} encountered for committing thread, which should not happen, according to this, state machine may not have further progress, stop the system {}", error, error.getMessage());
                System.exit(-1);
            }
        }
    }
}
