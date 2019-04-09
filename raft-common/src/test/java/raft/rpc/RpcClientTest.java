package raft.rpc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import raft.RaftMessageType;
import raft.RaftRequestMessage;
import raft.RaftResponseMessage;
import rpc.RpcClient;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gbs on 19/3/27.
 */
public class RpcClientTest {

    private int serverPort = 12345;

    private ExecutorService executorService;

    RpcClient rpcClient;


    @Before
    public void before() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        rpcClient = new RpcClient(new InetSocketAddress(serverPort),executorService);
    }

    @After
    public void after() {
        executorService.shutdown();
        executorService = null;
    }


    @Test
    public void testClient() throws ExecutionException, InterruptedException {
        RaftRequestMessage requestMessage = new RaftRequestMessage();
        requestMessage.setMessageType(RaftMessageType.RequestVoteRequest);
        requestMessage.setCommitIndex(1);
        requestMessage.setLastLogIndex(1);
        requestMessage.setLastLogTerm(1);
        requestMessage.setDestination(1);
        requestMessage.setSource(2);
        requestMessage.setTerm(1);

        CompletableFuture<RaftResponseMessage> send = rpcClient.send(requestMessage);

        RaftResponseMessage response = send.get();

        String log = String.format(
                "Response back a %s message to %d with Accepted=%s, Term=%d, NextIndex=%d",
                response.getMessageType().toString(),
                response.getDestination(),
                String.valueOf(response.isGranted()),
                response.getTerm(),
                response.getNextIndex());
        System.out.println(log);
    }

    @Test
    public void testClients() throws ExecutionException, InterruptedException {
        for(int i = 0;i<10;i++){
            RaftRequestMessage requestMessage = new RaftRequestMessage();
            requestMessage.setMessageType(RaftMessageType.RequestVoteRequest);
            requestMessage.setCommitIndex(i);
            requestMessage.setLastLogIndex(i);
            requestMessage.setLastLogTerm(i);
            requestMessage.setDestination(i);
            requestMessage.setSource(2);
            requestMessage.setTerm(i);

            CompletableFuture<RaftResponseMessage> send = rpcClient.send(requestMessage);

            RaftResponseMessage response = send.get();

            String log = String.format(
                    "Response back a %s message to %d with Accepted=%s, Term=%d, NextIndex=%d",
                    response.getMessageType().toString(),
                    response.getDestination(),
                    String.valueOf(response.isGranted()),
                    response.getTerm(),
                    response.getNextIndex());
            System.out.println(log);
        }
    }



}
