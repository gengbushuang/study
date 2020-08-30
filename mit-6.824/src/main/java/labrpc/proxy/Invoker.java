package labrpc.proxy;

import labrpc.Transporter;
import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

public class Invoker implements RpcInvocationHandler {

    private final AtomicLong requestCounter = new AtomicLong();

    private final Transporter<RpcRequest, RpcResponse> transporter;

    private final Class<?> protocol;

    public Invoker(Class<?> _protocol, Transporter transporter) {
        this.protocol = _protocol;
        this.transporter = transporter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest(); // 创建并初始化 RPC 请求
        request.setVersion(1234);
        request.setRequestId(protocol.getName() + "-" + requestCounter.incrementAndGet());
        request.setClassName(protocol.getName());
        request.setMethodName(method.getName());
        request.setParameterClasses(method.getParameterTypes());
        request.setParameters(args);

        RpcResponse response = transporter.send(request);
        if (response.getError() != null) {
            throw response.getError();
        }

        return response.getResult();
    }
}
