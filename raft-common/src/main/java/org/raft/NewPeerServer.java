package org.raft;

import raft.ClusterServer;
import raft.RaftMessageType;
import raft.RaftRequestMessage;
import raft.RaftResponseMessage;
import raft.exception.RpcException;
import rpc.RpcClient;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Created by gbs on 19/4/4.
 */
public class NewPeerServer {

    private AtomicInteger busyFlag;
    private AtomicInteger pendingCommitFlag;


    private RpcClient rpcClient;
    private ClusterServer clusterServer;

    //心跳
    private ScheduledFuture<?> heartbeatTask;
    private Callable<Void> heartbeatTimeoutHandler;

    private Executor executor;

    private boolean heartbeatEnabled;

    private int currentHeartbeatInterval;
    private int heartbeatInterval;
    private int maxHeartbeatInterval;
    private int rpcBackoffInterval;

    private long nextLogIndex;
    private long matchedIndex;

    public NewPeerServer(ClusterServer server, NewRaftContext raftContext, final Consumer<NewPeerServer> heartbeatConsumer) {

        this.clusterServer = server;
        //rpc客户端
        this.rpcClient = raftContext.getRpcClientFactory().createRpcClient(server.getEndpoint());

        this.executor = raftContext.getScheduledExecutor();

        this.busyFlag = new AtomicInteger(0);
        this.pendingCommitFlag = new AtomicInteger(0);

        this.heartbeatEnabled = false;
        this.heartbeatInterval = this.currentHeartbeatInterval = raftContext.getRaftOptions().getHeartbeatPeriodMilliseconds();
        this.maxHeartbeatInterval = raftContext.getRaftOptions().getMaxHeartbeatInterval();
        this.rpcBackoffInterval = raftContext.getRaftOptions().getRpcFailureBackoff();

        NewPeerServer peerServer = this;

        this.nextLogIndex = 1;
        this.matchedIndex = 0;

        heartbeatTimeoutHandler = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                heartbeatConsumer.accept(peerServer);
                return null;
            }
        };
    }

    public int getId() {
        return this.clusterServer.getId();
    }

    public CompletableFuture<RaftResponseMessage> sendRequest(RaftRequestMessage requestMessage) {
        boolean isAppendRequest = requestMessage.getMessageType() == RaftMessageType.AppendEntriesRequest;
        return rpcClient
                .send(requestMessage)
                .thenComposeAsync((RaftResponseMessage response) -> {
                    if (isAppendRequest) {
                        this.setFree();
                    }
                    this.resumeHeartbeatingSpeed();
                    return CompletableFuture.completedFuture(response);
                }, this.executor)
                .exceptionally((Throwable err) -> {
                    if (isAppendRequest) {
                        this.setFree();
                    }
                    this.slowDownHeartbeating();
                    throw new RpcException(err, requestMessage);
                });
    }


    public boolean makeBusy() {
        return this.busyFlag.compareAndSet(0, 1);
    }

    public void setFree() {
        this.busyFlag.set(0);
    }

    public void setPendingCommit(){
        this.pendingCommitFlag.set(1);
    }

    public boolean clearPendingCommit(){
        return this.pendingCommitFlag.compareAndSet(1, 0);
    }

    public boolean isHeartbeatEnabled() {
        return this.heartbeatEnabled;
    }

    public Callable<Void> getHeartbeartHandler() {
        return this.heartbeatTimeoutHandler;
    }

    public void setHeartbeatTask(ScheduledFuture<?> heartbeatTask) {
        this.heartbeatTask = heartbeatTask;
    }

    public ScheduledFuture<?> getHeartbeatTask() {
        return this.heartbeatTask;
    }


    public void enableHeartbeat(boolean enable) {
        this.heartbeatEnabled = enable;
        if (!enable) {
            heartbeatTask = null;
        }
    }

    public synchronized int getCurrentHeartbeatInterval() {
        return this.currentHeartbeatInterval;
    }

    public synchronized void slowDownHeartbeating() {
        this.currentHeartbeatInterval = Math.min(this.maxHeartbeatInterval, this.currentHeartbeatInterval + this.rpcBackoffInterval);
    }

    public synchronized void resumeHeartbeatingSpeed() {
        if (this.currentHeartbeatInterval > this.heartbeatInterval) {
            this.currentHeartbeatInterval = this.heartbeatInterval;
        }
    }

    public long getNextLogIndex() {
        return nextLogIndex;
    }

    public void setNextLogIndex(long nextLogIndex) {
        this.nextLogIndex = nextLogIndex;
    }

    public long getMatchedIndex() {
        return matchedIndex;
    }

    public void setMatchedIndex(long matchedIndex) {
        this.matchedIndex = matchedIndex;
    }
}
