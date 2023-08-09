package eapli.base.strings.util;

public class StringSplitter {

    public static String getFirstPartOfEmail(String email) {
        String[] parts = email.split("@");
        if (parts.length > 0) {
            return parts[0];
        } else {
            // Invalid email address format
            return "";
        }
    }
}
