package com.pattern.behavior.observer.base;

public class Demo {

    public static void main(String[] args) {
        Subject subject = new ListSubject();
        subject.registerObserver(new ObserverOne());
        subject.registerObserver(new ObserverTwo());

        subject.notifyObservers("test");
    }
}
