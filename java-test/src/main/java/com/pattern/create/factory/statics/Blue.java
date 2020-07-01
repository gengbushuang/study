package com.pattern.create.factory.statics;

public class Blue extends Color {
    @Override
    public Color newInstance() {
        return new Blue();
    }
}
