package com.pattern.behavior.observer.base;

import java.util.ArrayList;
import java.util.List;

public class ListSubject implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String msg) {
        for (Observer observer : observers) {
            observer.update(msg);
        }
    }
}
