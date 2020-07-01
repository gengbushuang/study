package com.pattern.create.factory.statics;

public class Black extends Color {
    @Override
    public Color newInstance() {
        return new Black();
    }
}
