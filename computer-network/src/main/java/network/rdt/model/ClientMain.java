package network.rdt.model;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import network.aio.Client;

public class ClientMain {
	public static void main(String[] args) throws IOException {
		int port = 12345;
		InetSocketAddress remote = new InetSocketAddress("127.0.0.1", port);
		int processors = Runtime.getRuntime().availableProcessors();
		Client client = new Client(remote, Executors.newFixedThreadPool(processors));
		
		client.rtd_send("客户端发送");
	}
}
