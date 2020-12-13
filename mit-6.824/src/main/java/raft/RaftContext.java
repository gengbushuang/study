package raft;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class RaftContext {

    private ScheduledThreadPoolExecutor scheduledExecutor;

    private RaftOptions raftOptions;

    private RpcClientFactory rpcClientFactory;

    private ClusterConfiguration configuration;

    private int sid;

    public RaftContext(int sid,RaftOptions raftOptions,ClusterConfiguration configuration, RpcClientFactory rpcClientFactory) {
        this(sid,raftOptions,configuration, rpcClientFactory, null);
    }

    public RaftContext(int sid,RaftOptions raftOptions,ClusterConfiguration configuration, RpcClientFactory rpcClientFactory, ScheduledThreadPoolExecutor scheduledExecutor) {
        this.sid = sid;
        this.rpcClientFactory = rpcClientFactory;
        this.scheduledExecutor = scheduledExecutor;

        this.configuration = configuration;

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

    public ClusterConfiguration getConfiguration() {
        return configuration;
    }

    public int getSid() {
        return sid;
    }
}
