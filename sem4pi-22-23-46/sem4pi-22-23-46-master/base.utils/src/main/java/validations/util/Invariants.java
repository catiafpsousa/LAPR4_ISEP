package validations.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import eapli.framework.actions.Actions;

import java.util.Collection;
import java.util.regex.Pattern;

public final class Invariants {
    private Invariants() {
    }

    public static void areEqual(final long a, final long b) {
        Validations.ensureAreEqual(a, b, Actions.THROW_STATE);
    }

    public static void areEqual(final Object a, final Object b, final String msg) {
        Validations.ensureAreEqual(a, b, Actions.throwState(msg));
    }

    public static void matches(final Pattern regex, final String arg, final String msg) {
        Validations.ensureMatches(regex, arg, Actions.throwState(msg));
    }

    public static void ensure(final boolean b) {
        Validations.ensure(b, Actions.THROW_STATE);
    }

    public static void ensure(final boolean b, final String msg) {
        Validations.ensure(b, Actions.throwState(msg));
    }

    public static void nonNull(final Object arg) {
        Validations.ensureNoneNull(Actions.THROW_STATE, new Object[]{arg});
    }

    public static void nonNull(final Object arg, final String msg) {
        Validations.ensureNoneNull(Actions.throwState(msg), new Object[]{arg});
    }

    public static void noneNull(final Object... arg) {
        Validations.ensureNoneNull(Actions.THROW_STATE, arg);
    }

    public static void nonEmpty(final Collection<?> items) {
        Validations.ensureNonEmpty(items, Actions.THROW_STATE);
    }

    public static void nonEmpty(final Collection<?> items, final String msg) {
        Validations.ensureNonEmpty(items, Actions.throwState(msg));
    }

    public static void nonEmpty(final String arg) {
        Validations.ensureNonEmpty(arg, Actions.THROW_STATE);
    }

    public static void nonEmpty(final String arg, final String msg) {
        Validations.ensureNonEmpty(arg, Actions.throwState(msg));
    }

    public static void isPositive(final long arg) {
        Validations.ensureIsPositive(arg, Actions.THROW_STATE);
    }

    public static void isPositive(final long arg, final String msg) {
        Validations.ensureIsPositive(arg, Actions.throwState(msg));
    }

    public static void nonNegative(final long arg) {
        Validations.ensureNonNegative(arg, Actions.THROW_STATE);
    }

    public static void nonNegative(final long arg, final String msg) {
        Validations.ensureNonNegative(arg, Actions.throwState(msg));
    }
}
