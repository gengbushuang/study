package com.pattern.behavior.observer.base;

public interface Subject {

    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(String msg);
}
