package com.threadtest.pattern.composition1;

/**
 * 适配类
 *
 */
public class AdaptedTank implements Tank {

    protected final Tank delegate;

    public AdaptedTank(Tank t) {
        this.delegate = t;
    }

    @Override
    public float getCapacity() {
        return delegate.getCapacity();
    }

    @Override
    public float getVolume() {
        return delegate.getVolume();
    }

    protected void checkVolumeInvariant() throws AssertionError {
        float v = this.getVolume();
        float c = this.getCapacity();
        if (!(v >= 0.0 && v <= c)) {
            throw new AssertionError();
        }
    }

    @Override
    public synchronized void transferWater(float amount) throws OverflowException, UnderflowException {
        this.checkVolumeInvariant();
        try {
            delegate.transferWater(amount);
        }catch (OverflowException e){
            throw e;
        }catch (UnderflowException e){
            throw e;
        }finally {
            this.checkVolumeInvariant();
        }
    }
}
