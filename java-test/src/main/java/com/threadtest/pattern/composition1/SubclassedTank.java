package com.threadtest.pattern.composition1;

/**
 * 子类化
 */
public class SubclassedTank extends TankImpl {

    protected void checkVolumeInvariant() throws AssertionError {

    }

    @Override
    public synchronized void transferWater(float amount) throws OverflowException, UnderflowException {
        this.checkVolumeInvariant();
        try {
            super.transferWater(amount);
        } catch (OverflowException e) {
            throw e;
        } catch (UnderflowException e) {
            throw e;
        } finally {
            this.checkVolumeInvariant();
        }

    }
}
