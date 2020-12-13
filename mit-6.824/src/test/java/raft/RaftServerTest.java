package raft;

import labrpc.impl.RpcServer;
import raft.impl.RaftNodeServiceImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

public class RaftServerTest {

    public void testServer1() throws URISyntaxException {
        int sid = 1;
        RaftContext context = new RaftContext(sid, getOptions(), getConfiguration(), getClientFactory());
        RaftNodeService raftNodeService = new RaftNodeServiceImpl(context);
        ClusterConfiguration configuration = context.getConfiguration();
        ClusterServer server = configuration.getServer(sid);
        URI localEndpoint = new URI(server.getEndpoint());
        RpcServer rpcServer = new RpcServer(localEndpoint.getPort());
        rpcServer.register(raftNodeService);

    }

    public void testServer2() throws URISyntaxException {
        int sid = 2;
        RaftContext context = new RaftContext(sid, getOptions(), getConfiguration(), getClientFactory());
        RaftNodeService raftNodeService = new RaftNodeServiceImpl(context);
        ClusterConfiguration configuration = context.getConfiguration();
        ClusterServer server = configuration.getServer(sid);
        URI localEndpoint = new URI(server.getEndpoint());
        RpcServer rpcServer = new RpcServer(localEndpoint.getPort());
        rpcServer.register(raftNodeService);
    }

    public void testServer3() throws URISyntaxException {
        int sid = 3;
        RaftContext context = new RaftContext(sid, getOptions(), getConfiguration(), getClientFactory());
        RaftNodeService raftNodeService = new RaftNodeServiceImpl(context);
        ClusterConfiguration configuration = context.getConfiguration();
        ClusterServer server = configuration.getServer(sid);
        URI localEndpoint = new URI(server.getEndpoint());
        RpcServer rpcServer = new RpcServer(localEndpoint.getPort());
        rpcServer.register(raftNodeService);
    }

    private RpcClientFactory getClientFactory() {
        RpcClientFactory rpcClientFactory = new RpcClientFactory(Executors.newSingleThreadExecutor());
        return rpcClientFactory;
    }

    private RaftOptions getOptions() {
        RaftOptions options = new RaftOptions();
        options.setElectionTimeoutLowerBound(45000);
        options.setElectionTimeoutUpperBound(60000);
        options.setHeartbeatInterval(30000);
        return options;
    }

    private ClusterConfiguration getConfiguration() {
        ClusterServer clusterServer1 = new ClusterServer(1, "tcp://localhost:9001");
        ClusterServer clusterServer2 = new ClusterServer(2, "tcp://localhost:9002");
        ClusterServer clusterServer3 = new ClusterServer(3, "tcp://localhost:9003");

        ClusterConfiguration clusterConfiguration = new ClusterConfiguration();
        clusterConfiguration.addServers(clusterServer1);
        clusterConfiguration.addServers(clusterServer2);
        clusterConfiguration.addServers(clusterServer3);
        return clusterConfiguration;
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        RaftServerTest serverTest = new RaftServerTest();
//        serverTest.testServer1();
//        serverTest.testServer2();
        serverTest.testServer3();

        Thread.sleep(Integer.MAX_VALUE);
    }

}
