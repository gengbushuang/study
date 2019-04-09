package raft.rpc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import raft.RaftMessageHandler;
import rpc.RpcServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gbs on 19/3/27.
 */
public class RpcServerTest {

    private int serverPort = 12345;

    private ExecutorService executorService;

    final RaftMessageHandler messageHandler = new RaftMessageHandlerTest();

    @Before
    public void before() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    @After
    public void after() {
        executorService.shutdown();
        executorService = null;
    }

    @Test
    public void testService(){

        RpcServer rpcServer = new RpcServer(serverPort, executorService);

        rpcServer.listener(messageHandler);
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
