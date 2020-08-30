package raft;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RaftContext {

    private ScheduledThreadPoolExecutor scheduledExecutor;

    private RaftOptions raftOptions;

    private RpcClientFactory rpcClientFactory;

    public RaftContext(RaftOptions raftOptions, RpcClientFactory rpcClientFactory, ScheduledThreadPoolExecutor scheduledExecutor) {
        this.rpcClientFactory = rpcClientFactory;
        this.scheduledExecutor = scheduledExecutor;
        if (this.scheduledExecutor == null) {
            this.scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        }

        if (this.raftOptions == null) {
            RaftOptions options = new RaftOptions();
            options.setElectionTimeoutLowerBound(1500);
            options.setElectionTimeoutUpperBound(3000);
            options.setHeartbeatInterval(1000);
            this.raftOptions = options;
        }
    }

    public ScheduledThreadPoolExecutor getScheduledExecutor() {
        return scheduledExecutor;
    }

    public RaftOptions getRaftOptions() {
        return raftOptions;
    }

    public RpcClientFactory getRpcClientFactory() {
        return rpcClientFactory;
    }
}
