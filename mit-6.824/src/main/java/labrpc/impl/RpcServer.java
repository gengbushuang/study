package labrpc.impl;

import labrpc.ByteDecode;
import labrpc.ByteEncoder;
import labrpc.aio.AioServer;
import labrpc.aio.Dispatcher;
import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;
import labrpc.router.Router;
import labrpc.router.RoutingClassRegistry;

import java.util.concurrent.Executors;

public class RpcServer {

    private final AioServer server;

    private final RpcRouter rpcRouter;

    public RpcServer(int port) {
        server = new AioServer(port, Executors.newSingleThreadExecutor());
        this.rpcRouter = new RpcRouter(new RpcResponseDecode(), new RpcRequestEncoder());
        server.startListening(this.rpcRouter);
    }

    public void register(Object o) {
        rpcRouter.register(o);
    }


    private class RpcRouter implements Dispatcher {

        private ByteDecode<RpcResponse> decoder;

        private ByteEncoder<RpcRequest> encoder;

        private RoutingClassRegistry classRegistry = new RoutingClassRegistry();

        public RpcRouter(ByteDecode decoder, ByteEncoder encoder) {
            this.decoder = decoder;
            this.encoder = encoder;
        }

        public void register(Object bean) {
            classRegistry.register(bean);
        }

        @Override
        public byte[] processByte(byte[] data) {
            return processRequest(data);
        }

        private byte[] processRequest(byte[] data) {
            RpcRequest request = this.encoder.encoder(data);
            RpcResponse response = processResponse(request);
            return this.decoder.decode(response);
        }

        private RpcResponse processResponse(RpcRequest request) {
            RpcResponse response = new RpcResponse();
            response.setRequestId(request.getRequestId());
            response.setVersion(request.getVersion());
            try {
                Router router = classRegistry.find(request.getClassName(), request.getMethodName());
                Object invoke = router.invoke(request.getParameterClasses(), request.getParameters());
                response.setResult(invoke);
            } catch (Throwable e) {
                response.setError(e);
            }
            return response;
        }
    }
}
