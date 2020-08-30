package labrpc.router;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class RoutingClassRegistry {
    private final Map<String, RoutingMethod> interfaceRegistry = new HashMap<>();

    public void register(Object bean) {
        Class<?> beanClass = bean.getClass();
        Class<?>[] interfaces = beanClass.getInterfaces();
        for (Class<?> inter : interfaces) {
            addClassInterface(inter, bean);
        }
    }

    private <T> void addClassInterface(Class<T> cls, Object bean) {
        if (cls.isInterface()) {
            if (hasClass(cls.getName())) {

            }
            RoutingMethod routingMethod = new RoutingMethod(cls, bean);
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
                    routingMethod.addMethod(method);
                }
            }
            interfaceRegistry.put(cls.getName(), routingMethod);
        }
    }

    private boolean hasClass(String clasName) {
        return interfaceRegistry.containsKey(clasName);
    }

    public Router find(String clasName, String methodName) throws ClassNotFoundException, NoSuchMethodException {
        if (!interfaceRegistry.containsKey(clasName)) {
            throw new ClassNotFoundException(clasName);
        }
        RoutingMethod routingMethod = interfaceRegistry.get(clasName);
        Method method = routingMethod.find(methodName);
        return Router.create(routingMethod.getInvokeTarget(), method);
    }
}
