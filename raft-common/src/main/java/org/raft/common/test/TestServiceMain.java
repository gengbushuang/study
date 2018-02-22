package org.raft.common.test;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.raft.common.server.RpcTcpListener;

public class TestServiceMain {

	public static void main(String[] args) throws IOException {
		TestMessage message = new TestMessage();
		int port = 12345;
		int processors = Runtime.getRuntime().availableProcessors();
		RpcTcpListener listener = new RpcTcpListener(port, Executors.newFixedThreadPool(processors));
		listener.startListening(message);
		System.in.read();
		listener.stop();
	}
}
