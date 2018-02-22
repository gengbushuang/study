package org.raft.common.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.raft.common.client.RpcTcpClient;
import org.raft.common.message.RaftMessageType;
import org.raft.common.message.RaftRequestMessage;
import org.raft.common.message.RaftResponseMessage;

public class TestClientMain {
	public static void main(String[] args) throws IOException {
		int port = 12345;
		InetSocketAddress remote = new InetSocketAddress("127.0.0.1", port);
		int processors = Runtime.getRuntime().availableProcessors();
		RpcTcpClient tcpClient = new RpcTcpClient(remote, Executors.newFixedThreadPool(processors));
		
		RaftRequestMessage request = new RaftRequestMessage();
		request.setTerm(1);
		request.setSource(4);
		request.setDestination(2);
		request.setCommitIndex(3);
		request.setLastLogIndex(5);
		request.setMessageType(RaftMessageType.AppendEntriesRequest);
		request.setLastLogTerm(7);
		for(int i = 0;i<10;i++){
		CompletableFuture<RaftResponseMessage> send = tcpClient.send(request);
		try {
			RaftResponseMessage raftResponseMessage = send.get();
			System.out.println(raftResponseMessage);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		}
	}
}
