package raft;

import labrpc.impl.RpcClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

public class RaftNodeClient {

    private Logger logger = LogManager.getLogger(getClass());

    private RpcClient rpcClient;

    private RaftNodeService proxy;

    private int serviceId;

    public RaftNodeClient(int sid, String endpoint, RaftContext raftContext) {
        this.serviceId = sid;
        this.rpcClient = raftContext.getRpcClientFactory().createRpcClient(endpoint);
        this.proxy = this.rpcClient.getProxy(RaftNodeService.class);
    }

    public int getServiceId() {
        return serviceId;
    }

    public void SendRequest(Consumer<raft.RaftNodeService> consumer) {
        consumer.accept(proxy);
    }
}
