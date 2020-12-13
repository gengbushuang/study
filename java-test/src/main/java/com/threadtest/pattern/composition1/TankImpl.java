package com.threadtest.pattern.composition1;

public class TankImpl implements Tank {
    @Override
    public float getCapacity() {
        return 0;
    }

    @Override
    public float getVolume() {
        return 0;
    }

    @Override
    public void transferWater(float amount) throws OverflowException, UnderflowException {

    }
}
