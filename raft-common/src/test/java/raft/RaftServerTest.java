package raft;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.raft.NewRaftContext;
import org.raft.NewRaftServer;
import raft.file.FileServerStateManager;
import raft.file.FileStateMachine;
import rpc.RpcServer;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

/**
 * Created by gbs on 19/4/5.
 */
public class RaftServerTest {

    RaftOptions raftOptions;

    @Before
    public void before() throws URISyntaxException {

        raftOptions = new RaftOptions();
        raftOptions.setElectionTimeoutLowerBound(1500);
        raftOptions.setElectionTimeoutUpperBound(3000);
        raftOptions.setHeartbeatPeriodMilliseconds(750);
        raftOptions.setMaxAppendingSize(100);
        raftOptions.setRpcFailureBackoff(250);

    }
    @After
    public void after(){


    }

    @Test
    public void testServer1() throws URISyntaxException {
        String path = "/Users/gbs/tmp/tmp_raft/server1";
        int sid = 1;
        int port = 8001;

        FileServerStateManager fileServerStateManager = new FileServerStateManager(path);
        FileStateMachine fileStateMachine = new FileStateMachine(Paths.get(path), port);
        NewRaftContext context = new NewRaftContext(fileServerStateManager,fileStateMachine,raftOptions);
        NewRaftServer raftServer = new NewRaftServer(sid,context);


        ClusterConfiguration configuration = fileServerStateManager.loadClusterConfiguration();
        URI localEndpoint = new URI(configuration.getServer(sid).getEndpoint());

        RpcServer rpcServer = new RpcServer(localEndpoint.getPort(), context.getScheduledExecutor());

        rpcServer.listener(raftServer);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testServer2() throws URISyntaxException {
        String path = "/Users/gbs/tmp/tmp_raft/server2";
        int sid = 2;
        int port = 8002;

        FileServerStateManager fileServerStateManager = new FileServerStateManager(path);
        FileStateMachine fileStateMachine = new FileStateMachine(Paths.get(path), port);
        NewRaftContext context = new NewRaftContext(fileServerStateManager,fileStateMachine,raftOptions);
        NewRaftServer raftServer = new NewRaftServer(sid,context);


        ClusterConfiguration configuration = fileServerStateManager.loadClusterConfiguration();
        URI localEndpoint = new URI(configuration.getServer(sid).getEndpoint());

        RpcServer rpcServer = new RpcServer(localEndpoint.getPort(), context.getScheduledExecutor());

        rpcServer.listener(raftServer);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testServer3() throws URISyntaxException {
        String path = "/Users/gbs/tmp/tmp_raft/server3";
        int sid = 3;
        int port = 8003;

        FileServerStateManager fileServerStateManager = new FileServerStateManager(path);
        FileStateMachine fileStateMachine = new FileStateMachine(Paths.get(path), port);
        NewRaftContext context = new NewRaftContext(fileServerStateManager,fileStateMachine,raftOptions);
        NewRaftServer raftServer = new NewRaftServer(sid,context);


        ClusterConfiguration configuration = fileServerStateManager.loadClusterConfiguration();
        URI localEndpoint = new URI(configuration.getServer(sid).getEndpoint());

        RpcServer rpcServer = new RpcServer(localEndpoint.getPort(), context.getScheduledExecutor());

        rpcServer.listener(raftServer);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
