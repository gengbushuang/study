package org.raft.common.client;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;

public class RpcTcpClientFactory {

	private ExecutorService executorService;

	public RpcTcpClientFactory(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public RpcTcpClient createRpcClient(String endpoint) {
		try {
			URI uri = new URI(endpoint);
			return new RpcTcpClient(new InetSocketAddress(uri.getHost(), uri.getPort()), this.executorService);
		} catch (URISyntaxException e) {
			LogManager.getLogger(getClass()).error(String.format("%s is not a valid uri", endpoint));
			throw new IllegalArgumentException("invalid uri for endpoint");
		}
	}
}
