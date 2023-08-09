package math.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public final class NumberPredicates {
    private NumberPredicates() {
    }

    public static boolean isPrime(final long number) {
        if (isEven(number)) {
            return false;
        } else {
            for(long i = 3L; i * i <= number; i += 2L) {
                if (number % i == 0L) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isPositive(final long number) {
        return number > 0L;
    }

    public static boolean isNegative(final long number) {
        return number < 0L;
    }

    public static boolean isNonNegative(final long number) {
        return number >= 0L;
    }

    public static boolean isNonPositive(final long number) {
        return number <= 0L;
    }

    public static boolean isOdd(final long number) {
        return number % 2L != 0L;
    }

    public static boolean isEven(final long number) {
        return number % 2L == 0L;
    }

    public static boolean isWithinMargin(final double x, final double min, final double max) {
        return x >= min && x <= max;
    }

    public static boolean isGreater(long arg1, long arg2) {
        return arg2>arg1;
    }
}
