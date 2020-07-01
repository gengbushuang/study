package com.pattern.create.factory.statics;

public class Red extends Color {
    @Override
    public Color newInstance() {
        return new Red();
    }
}
