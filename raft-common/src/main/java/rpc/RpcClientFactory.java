package rpc;

import org.apache.logging.log4j.LogManager;

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
            return new RpcClient(new InetSocketAddress(uri.getHost(), uri.getPort()), this.executorService);
        } catch (URISyntaxException e) {
            LogManager.getLogger(getClass()).error(String.format("%s is not a valid uri", endpoint));
            throw new IllegalArgumentException("invalid uri for endpoint");
        }
    }
}
