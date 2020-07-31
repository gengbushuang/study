package com.pattern.behavior.observer.eventbus;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObserverRegistry {

    private ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();

    public void register(Object observer) {
        Map<Class<?>, ArrayList<ObserverAction>> observerActions = findAllObserverActions(observer);
        for (Map.Entry<Class<?>, ArrayList<ObserverAction>> entry : observerActions.entrySet()) {

            CopyOnWriteArraySet<ObserverAction> observerSet = registry.get(entry.getKey());
            if (observerSet == null) {
                registry.putIfAbsent(entry.getKey(), new CopyOnWriteArraySet());
                observerSet = registry.get(entry.getKey());
            }

            observerSet.addAll(entry.getValue());
        }
    }

    public List<ObserverAction> getSubscribers(Object event) {
        List<ObserverAction> observerActions = new ArrayList<>();
        List<Class<?>> eventTypes = getEventTypes(event.getClass());
        for (Class<?> eventClass : eventTypes) {
            CopyOnWriteArraySet<ObserverAction> actions = registry.get(eventClass);
            if (actions != null) {
                observerActions.addAll(actions);
            }
        }
        return observerActions;
    }

    private List<Class<?>> getEventTypes(Class<?> eventClass) {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> cls = eventClass; cls != null; cls = cls.getSuperclass()) {
            classes.add(cls);
            classes.addAll(Arrays.asList(cls.getInterfaces()));
        }
        return classes;
    }

    private Map<Class<?>, ArrayList<ObserverAction>> findAllObserverActions(Object observer) {
        Map<Class<?>, ArrayList<ObserverAction>> observerActions = new HashMap<>();
        Class<?> observerClass = observer.getClass();
        for (Method method : getSubscribeAnnotationMethod(observerClass)) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> eventType = parameterTypes[0];
            if (!observerActions.containsKey(eventType)) {
                observerActions.put(eventType, new ArrayList<>());
            }
            observerActions.get(eventType).add(new ObserverAction(observer, method));
        }

        return observerActions;
    }

    private List<Method> getSubscribeAnnotationMethod(Class<?> observerClass) {
        List<Method> annotaionts = new ArrayList<>();
        Method[] methods = observerClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                annotaionts.add(method);
            }
        }
        return annotaionts;
    }
}
