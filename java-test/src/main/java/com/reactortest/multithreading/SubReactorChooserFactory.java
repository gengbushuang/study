package com.reactortest.multithreading;

public interface SubReactorChooserFactory {


    SubReactorChooser newChooser(SubReactor [] subReactors);

    interface SubReactorChooser{
        SubReactor next();
    }
}
