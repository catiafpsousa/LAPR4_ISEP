package validations.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public final class ComparablePredicates {
    private ComparablePredicates() {
    }

    public static boolean isBetween(final long e, final long begin, final long end) {
        return e >= begin && e <= end;
    }

    public static <T extends Comparable<T>> boolean isBetween(final T e, final T begin, final T end) {
        return e.compareTo(begin) >= 0 && e.compareTo(end) <= 0;
    }

    public static <T extends Comparable<T>> boolean isGreaterThan(final T a, final T b) {
        return a.compareTo(b) > 0;
    }

    public static <T extends Comparable<T>> boolean isAtLeast(final T a, final T b) {
        return a.compareTo(b) >= 0;
    }

    public static <T extends Comparable<T>> boolean isLessThan(final T a, final T b) {
        return a.compareTo(b) < 0;
    }

    public static <T extends Comparable<T>> boolean isAtMost(final T a, final T b) {
        return a.compareTo(b) <= 0;
    }
}
