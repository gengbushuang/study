package labrpc;

import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Transporter<ReqT, RespT> {

    RespT send(ReqT message) throws ExecutionException, InterruptedException, TimeoutException;

    CompletableFuture<RespT> sendSync(ReqT message);


}
