package labrpc.router;

import labrpc.mobel.RpcRequest;
import labrpc.mobel.RpcResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Router {

    private final Object target;
    private final Method method;

    public Router(Object bean, Method method) {
        this.target = bean;
        this.method = method;
    }

    public static Router create(Object invokeTarget, Method method) {
        return new Router(invokeTarget, method);
    }

    public Object invoke(Class<?>[] parameterClasses, Object[] parameters) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(target, parameters);
    }
}
