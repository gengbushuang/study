package raft;

import raft.exception.RpcException;
import rpc.RpcClient;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PeerServer {

    private ClusterServer clusterConfig;

    private RpcClient rpcClient;

    private AtomicInteger busyFlag;
    private AtomicInteger pendingCommitFlag;

    private boolean heartbeatEnabled;

    private int currentHeartbeatInterval;
    private int heartbeatInterval;
    private int maxHeartbeatInterval;
    private int rpcBackoffInterval;


    private ScheduledFuture<?> heartbeatTask;

    /**
     * 心跳任务
     */
    private Callable<Void> heartbeatTimeoutHandler;

    private Executor executor;
    private long nextLogIndex;
    private long matchedIndex;


    public PeerServer(ClusterServer server,final Consumer<PeerServer> heartbeatConsumer) {
        this.clusterConfig = server;
        this.heartbeatTask = null;

        this.busyFlag = new AtomicInteger(0);

        PeerServer self = this;
        this.heartbeatTimeoutHandler = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                heartbeatConsumer.accept(self);
                return null;
            }
        };
    }

    public int getId() {
        return this.clusterConfig.getId();
    }

    public void setFree() {
        this.busyFlag.set(0);
    }

    public ScheduledFuture<?> getHeartbeatTask(){
        return this.heartbeatTask;
    }

    public synchronized int getCurrentHeartbeatInterval() {
        return this.currentHeartbeatInterval;
    }


    public Callable<Void> getHeartbeartHandler() {
        return this.heartbeatTimeoutHandler;
    }

    public void enableHeartbeat(boolean enable) {
        this.heartbeatEnabled = enable;
        if (!enable) {
            this.heartbeatTask = null;
        }
    }

    public synchronized void resumeHeartbeatingSpeed() {
        //如果当前心跳时间小于设置的心跳时间
        //重新赋值当前的心跳时间
        if (this.currentHeartbeatInterval > this.heartbeatInterval) {
            this.currentHeartbeatInterval = this.heartbeatInterval;
        }
    }

    public synchronized void slowDownHeartbeating() {
        this.currentHeartbeatInterval = Math.min(this.maxHeartbeatInterval, this.currentHeartbeatInterval + this.rpcBackoffInterval);
    }

    public void setHeartbeatTask(ScheduledFuture<?> heartbeatTask) {
        this.heartbeatTask = heartbeatTask;
    }

    public CompletableFuture<RaftResponseMessage> sendRequest(RaftRequestMessage request) {
        boolean isAppendRequest = request.getMessageType() == RaftMessageType.AppendEntriesRequest;
        CompletableFuture<RaftResponseMessage> send = this.rpcClient.send(request);
        return send
                .thenComposeAsync((RaftResponseMessage response) -> {
                    if (isAppendRequest) {
                        this.setFree();
                    }
                    this.resumeHeartbeatingSpeed();
                    return CompletableFuture.completedFuture(response);
                })

                .exceptionally((Throwable error) -> {
                    if (isAppendRequest) {
                        this.setFree();
                    }
                    this.slowDownHeartbeating();
                    throw new RpcException(error, request);
                })
                ;
    }

    public void setNextLogIndex(long nextLogIndex) {
        this.nextLogIndex = nextLogIndex;
    }

    public boolean makeBusy() {
        return this.busyFlag.compareAndSet(0, 1);
    }

    public long getNextLogIndex() {
        return nextLogIndex;
    }

    public void setPendingCommit() {
        this.pendingCommitFlag.set(1);
    }

    public long getMatchedIndex(){
        return this.matchedIndex;
    }

    public void setMatchedIndex(long matchedIndex){
        this.matchedIndex = matchedIndex;
    }
}
