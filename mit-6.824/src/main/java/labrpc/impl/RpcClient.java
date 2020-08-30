package labrpc.impl;

import labrpc.ByteDecode;
import labrpc.ByteEncoder;
import labrpc.Transporter;
import labrpc.aio.AioClient;
import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;
import labrpc.proxy.DefautRpcProxyFatory;
import labrpc.proxy.RpcProxy;
import labrpc.proxy.RpcProxyFatory;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class RpcClient {

    private final RpcChanal<RpcRequest, RpcResponse> chanal;

    private final RpcProxyFatory rpcProxyFatory = new DefautRpcProxyFatory();

    public RpcClient(String host, int port) {
        this(host, port, Executors.newSingleThreadExecutor());
    }

    public RpcClient(String host, int port, ExecutorService executorService) {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
        this.chanal = new RpcChanal<>(inetSocketAddress, executorService, new RpcRequestDecode(), new RpcResponseEncoder(), 50000);
    }

    private class RpcChanal<ReqT, RespT> implements Transporter<ReqT, RespT> {

        private ByteDecode<ReqT> byteDecode;

        private ByteEncoder<RespT> byteEncoder;

        private AioClient aioClient;

        private long timout;

        public RpcChanal(InetSocketAddress address, ByteDecode<ReqT> byteDecode, ByteEncoder<RespT> byteEncoder, long timeout) {
            this(address, Executors.newSingleThreadExecutor(), byteDecode, byteEncoder, timeout);
        }

        public RpcChanal(InetSocketAddress address, ExecutorService executorService, ByteDecode<ReqT> byteDecode, ByteEncoder<RespT> byteEncoder, long timeout) {
            aioClient = new AioClient(address, executorService);
            this.byteDecode = byteDecode;
            this.byteEncoder = byteEncoder;
            this.timout = timeout;
        }


        public RespT send(ReqT reqT) throws ExecutionException, InterruptedException, TimeoutException {
            byte[] decode = byteDecode.decode(reqT);
            CompletableFuture<byte[]> future = aioClient.send(decode);
            if (timout > 0) {
                return byteEncoder.encoder(future.get(timout, TimeUnit.MILLISECONDS));
            }
            return byteEncoder.encoder(future.get());
        }

        @Override
        public CompletableFuture<RespT> sendSync(ReqT reqT) {
            CompletableFuture<RespT> future = new CompletableFuture();
            try {
                RespT respT = this.send(reqT);
                future.complete(respT);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
            return future;
        }
    }


    public <T> T getProxy(Class<T> protocol) {
        RpcProxy<T> rpcProxy = rpcProxyFatory.getProxy(protocol, chanal);
        return rpcProxy.getProxy();
    }
}
