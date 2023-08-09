package eapli.base.strings.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringPredicates {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]*\\.[a-zA-Z]{2,6}$", 2);
    private static final String TRAILING_WHITESPACE_REGEX = ".*[ \t]+$";
    private static final String HEADING_WHITESPACE_REGEX = "^[ \t]+.*";

    private StringPredicates() {
    }

    public static boolean isEmail(final String text) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(text);
        return matcher.find();
    }

    public static boolean isPhrase(final String text) {
        return !isNullOrEmpty(text) && !text.matches("^[ \t]+.*") && !text.matches(".*[ \t]+$");
    }

    public static boolean isSingleWord(final String text) {
        return !isNullOrEmpty(text) && text.indexOf(32) == -1;
    }

    public static boolean isNullOrEmpty(final String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isNullOrWhiteSpace(final String text) {
        return text == null || text.isBlank();
    }

    public static boolean containsDigit(final String text) {
        return text.matches(".*\\d.*");
    }

    public static boolean containsAlpha(final String text) {
        return text.matches(".*[a-zA-Z].*");
    }

    public static boolean containsCapital(final String text) {
        return text.matches(".*[A-Z].*");
    }

    public static boolean containsAny(final String subject, final String chars) {
        return !isNullOrEmpty(chars) && !isNullOrEmpty(subject) ? subject.chars().anyMatch((c) -> {
            return chars.indexOf(c) != -1;
        }) : false;
    }
}

