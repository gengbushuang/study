package raft;

import labrpc.impl.RpcClient;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;

public class RpcClientFactory {

    private ExecutorService executorService;

    public RpcClientFactory(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public RpcClient createRpcClient(String endpoint) {
        try {
            URI uri = new URI(endpoint);
            return new RpcClient(uri.getHost(), uri.getPort(), this.executorService);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("invalid uri for endpoint");
        }
    }
}