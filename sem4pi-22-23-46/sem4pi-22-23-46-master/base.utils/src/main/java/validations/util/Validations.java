package validations.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import eapli.base.util.DateValidation;
import eapli.framework.actions.Action;
import eapli.framework.actions.Actions;

import eapli.framework.strings.util.StringPredicates;
import math.util.NumberPredicates;

import java.util.Calendar;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

class Validations {
    protected Validations() {
    }

    public static void ensureAreEqual(final long a, final long b, final Action consequence) {
        ensure(a == b, consequence);
    }

    public static void ensureAreEqual(final Object a, final Object b, final Action consequence) {
        ensure(Objects.equals(a, b), consequence);
    }

    public static void ensureMatches(final Pattern regex, final String arg, final Action consequence) {
        ensure(regex.matcher(arg).find(), consequence);
    }

    public static void ensure(final boolean test, final Action consequence) {
        Actions.doIfNot(consequence, test);
    }

    public static void ensureNoneNull(final Action consequence, final Object... objects) {
        Object[] var2 = objects;
        int var3 = objects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object each = var2[var4];
            Actions.doIfNot(consequence, Objects.nonNull(each));
        }

    }

    public static void ensureNonEmpty(final Collection<?> arg, final Action consequence) {
        ensure(arg != null && !arg.isEmpty(), consequence);
    }

    public static void ensureNonEmpty(final String arg, final Action consequence) {
        Actions.doIfNot(consequence, !StringPredicates.isNullOrWhiteSpace(arg));
    }

    public static void ensureIsPositive(final long arg, final Action consequence) {
        Actions.doIfNot(consequence, NumberPredicates.isPositive(arg));
    }

    public static void ensureNonNegative(final long arg, final Action consequence) {
        Actions.doIfNot(consequence, NumberPredicates.isNonNegative(arg));
    }

    public static void ensureIsGreater(long arg1, long arg2, Action consequence) {
        Actions.doIfNot(consequence, NumberPredicates.isGreater(arg1, arg2));
    }

    public static void ensureIsWithin(long arg1, long arg2, long arg3, Action consequence) {
        Actions.doIfNot(consequence, NumberPredicates.isWithinMargin(arg1, arg2, arg3));
    }

    public static void ensureIsInFuture(Calendar date, final Action consequence){
        Actions.doIfNot(consequence, DateValidation.isDateInFuture(date));
    }


    public static void ensureDateisAfter(Calendar date1, Calendar date2, final Action consequence) {
        Actions.doIfNot(consequence, DateValidation.isAfter(date1, date2));
    }
}
