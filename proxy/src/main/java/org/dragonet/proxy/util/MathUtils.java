package org.dragonet.proxy.util;

public class MathUtils {

    public static int floor(double n) {
        int i = (int) n;
        return n >= i ? i : i - 1;
    }

    public static int ceil(float floatNumber) {
        int truncated = (int) floatNumber;
        return floatNumber > truncated ? truncated + 1 : truncated;
    }
}
