package org.raft;

import raft.file.FileServerStateManager;
import raft.RaftOptions;
import raft.file.FileStateMachine;
import rpc.RpcClientFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by gbs on 19/4/4.
 */
public class NewRaftContext {

    private ScheduledThreadPoolExecutor scheduledExecutor;

    private RaftOptions raftOptions;

    private RpcClientFactory rpcClientFactory;

    private FileServerStateManager serverStateManager;

    private FileStateMachine stateMachine;


    public NewRaftContext(FileServerStateManager serverStateManager,FileStateMachine stateMachine,RaftOptions raftOptions) {
        this(serverStateManager,stateMachine,raftOptions, null, null);
    }

    public NewRaftContext(FileServerStateManager serverStateManager,FileStateMachine stateMachine,RaftOptions raftOptions, RpcClientFactory rpcClientFactory) {
        this(serverStateManager,stateMachine,raftOptions, rpcClientFactory, null);
    }

    public NewRaftContext(FileServerStateManager serverStateManager,FileStateMachine stateMachine,RaftOptions raftOptions, RpcClientFactory rpcClientFactory, ScheduledThreadPoolExecutor scheduledExecutor) {
        this.serverStateManager = serverStateManager;
        this.stateMachine = stateMachine;
        this.raftOptions = raftOptions;
        this.scheduledExecutor = scheduledExecutor;
        this.rpcClientFactory = rpcClientFactory;

        if (this.scheduledExecutor == null) {
            this.scheduledExecutor = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors() * 2);
        }

        if (this.rpcClientFactory == null) {
            this.rpcClientFactory = new RpcClientFactory(this.scheduledExecutor);
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

    public FileServerStateManager getServerStateManager() {
        return serverStateManager;
    }

    public FileStateMachine getStateMachine() {
        return stateMachine;
    }
}
