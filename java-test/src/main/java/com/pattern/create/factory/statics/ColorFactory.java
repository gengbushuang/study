package com.pattern.create.factory.statics;

/**
 * 静态工厂
 */
public class ColorFactory {

    public static Color create(ColorType type) throws ClassNotFoundException {
        switch (type) {
            case Red:
                return new Red();
            case Blue:
                return new Blue();
            case Black:
                return new Black();
            default:
                throw new ClassNotFoundException(type.name());
        }
    }
}
