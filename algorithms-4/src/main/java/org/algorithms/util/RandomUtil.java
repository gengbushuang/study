package org.algorithms.util;

public class RandomUtil {

    private final java.util.Random random;

    public RandomUtil() {
        this(new java.util.Random());
    }

    public RandomUtil(java.util.Random random) {
        this.random = random;
    }

    /**
     * min和max区间的随机数包含min和max
     * @param min
     * @param max
     * @return
     */
    public int randint(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
}
