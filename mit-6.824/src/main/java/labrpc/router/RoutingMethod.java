package labrpc.router;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RoutingMethod {

    private final Class<?> clazz;

    private final Object invokeTarget;

    private final Map<String, Method> methodRegistry = new HashMap<>();

    public RoutingMethod(Class<?> cls, Object bean) {
        this.clazz = cls;
        this.invokeTarget = bean;
    }


    public void addMethod(Method method) {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        methodRegistry.put(method.getName(), method);
    }

    public Object getInvokeTarget() {
        return invokeTarget;
    }

    public Method find(String methodName) throws NoSuchMethodException {
        if(!methodRegistry.containsKey(methodName)){
            throw new NoSuchMethodException(methodName);
        }
        return methodRegistry.get(methodName);
    }
}
