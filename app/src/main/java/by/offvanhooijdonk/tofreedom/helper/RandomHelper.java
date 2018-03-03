package by.offvanhooijdonk.tofreedom.helper;

import java.util.Random;

public class RandomHelper {
    private static final Random RANDOM = new Random();
    public static int randomize(int base, float radius) {
        return (int) (base * (1 + (RANDOM.nextFloat() - 0.5) * radius));
    }
}
