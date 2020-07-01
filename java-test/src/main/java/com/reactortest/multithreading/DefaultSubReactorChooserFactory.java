package com.reactortest.multithreading;

import java.util.concurrent.atomic.AtomicInteger;

public class DefaultSubReactorChooserFactory implements SubReactorChooserFactory {

    public static final DefaultSubReactorChooserFactory INSTANCE = new DefaultSubReactorChooserFactory();

    @Override
    public SubReactorChooser newChooser(SubReactor[] subReactors) {
        if (isPowerOfTwo(subReactors.length)) {
            return new PowerOfTwoSubReactorChooser(subReactors);
        } else {
            return new GenericSubReactorChooser(subReactors);
        }
    }

    //判断数字是否2的次方
    private static boolean isPowerOfTwo(int val) {
        //例如8
        //00001000 & 11111000 = 00001000
        return (val & -val) == val;
    }


    private static final class PowerOfTwoSubReactorChooser implements SubReactorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final SubReactor[] subReactors;

        PowerOfTwoSubReactorChooser(SubReactor[] subReactors) {
            this.subReactors = subReactors;
        }

        @Override
        public SubReactor next() {
            return subReactors[idx.getAndIncrement() & subReactors.length - 1];
        }
    }

    private static final class GenericSubReactorChooser implements SubReactorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final SubReactor[] subReactors;

        GenericSubReactorChooser(SubReactor[] subReactors) {
            this.subReactors = subReactors;
        }

        @Override
        public SubReactor next() {
            return subReactors[Math.abs(idx.getAndIncrement() % subReactors.length)];
        }
    }
}
