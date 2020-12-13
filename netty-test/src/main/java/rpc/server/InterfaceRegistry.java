package rpc.server;

import rpc.model.RpcRequest;

import java.util.HashMap;
import java.util.Map;

public class InterfaceRegistry {
    private static final InterfaceRegistry _instance = new InterfaceRegistry();
    private final Map<String, Object> interfaceMap = new HashMap<>();


    private InterfaceRegistry() {
    }

    public static InterfaceRegistry getInstance() {
        return _instance;
    }

    public void registry(Object bena) {
        Class<?>[] interfaces = bena.getClass().getInterfaces();
        for (Class<?> inter : interfaces) {
            String name = inter.getName();
            interfaceMap.put(name, bena);
        }
    }

    public Object findObject(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        if (!interfaceMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        return interfaceMap.get(className);
    }
}
