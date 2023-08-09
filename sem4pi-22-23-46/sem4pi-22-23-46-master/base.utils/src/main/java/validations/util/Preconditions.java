package validations.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import eapli.framework.actions.Actions;


import java.util.Calendar;
import java.util.Collection;
import java.util.regex.Pattern;

public final class Preconditions {
    private Preconditions() {
    }

    public static void noneNull(final Object... arg) {
        Validations.ensureNoneNull(Actions.throwArgument("At least one of the required method parameters is null"), arg);
    }

    public static void nonNegative(final long arg) {
        Validations.ensureNonNegative(arg, Actions.THROW_ARGUMENT);
    }

    public static void nonNegative(final long arg, final String msg) {
        Validations.ensureNonNegative(arg, Actions.throwArgument(msg));
    }

    public static void areEqual(final long a, final long b) {
        Validations.ensureAreEqual(a, b, Actions.THROW_ARGUMENT);
    }

    public static void areEqual(final Object a, final Object b, final String msg) {
        Validations.ensureAreEqual(a, b, Actions.throwArgument(msg));
    }

    public static void matches(final Pattern regex, final String arg, final String msg) {
        Validations.ensureMatches(regex, arg, Actions.throwArgument(msg));
    }

    public static void ensure(final boolean b) {
        ensure(b, "");
    }

    public static void ensure(final boolean b, final String msg) {
        Validations.ensure(b, Actions.throwArgument(msg));
    }

    public static void isPositive(final long arg) {
        Validations.ensureIsPositive(arg, Actions.THROW_ARGUMENT);
    }

    public static void isPositive(final long arg, final String msg) {
        Validations.ensureIsPositive(arg, Actions.throwArgument(msg));
    }

    public static void isGreater(final long arg1, final long arg2, final String msg) {
        Validations.ensureIsGreater(arg1, arg2, Actions.throwArgument(msg));
    }



    public static void nonNull(final Object arg) {
        Validations.ensureNoneNull(Actions.THROW_ARGUMENT, new Object[]{arg});
    }

    public static void nonNull(final Object arg, final String msg) {
        Validations.ensureNoneNull(Actions.throwArgument(msg), new Object[]{arg});
    }

    public static void nonEmpty(final Collection<?> items) {
        Validations.ensureNonEmpty(items, Actions.THROW_ARGUMENT);
    }

    public static void nonEmpty(final Collection<?> items, final String msg) {
        Validations.ensureNonEmpty(items, Actions.throwArgument(msg));
    }

    public static void nonEmpty(final String arg) {
        Validations.ensureNonEmpty(arg, Actions.THROW_ARGUMENT);
    }

    public static void nonEmpty(final String arg, final String msg) {
        Validations.ensureNonEmpty(arg, Actions.throwArgument(msg));
    }

    public static void isWithinMargin(final long arg1, final long arg2, final long arg3, final String msg) {
        Validations.ensureIsWithin(arg1, arg2, arg3, Actions.throwArgument(msg));
    }

    public static void isInTheFuture(final Calendar date, final String msg){
        Validations.ensureIsInFuture(date, Actions.throwArgument(msg));
    }

    public static void isAfter(final Calendar date1 , final Calendar date2, final String msg){
        Validations.ensureDateisAfter(date1, date2, Actions.throwArgument(msg));

    }

    public static void isTrue(final boolean condition, final String msg) {
        Validations.ensure(condition, Actions.throwArgument(msg));
    }



}

