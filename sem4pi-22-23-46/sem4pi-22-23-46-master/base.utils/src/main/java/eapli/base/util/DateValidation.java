package eapli.base.util;

import java.util.Calendar;

public class DateValidation {
    public static boolean isDateInFuture(Calendar date) {
        Calendar currentDate = Calendar.getInstance();
        return date.after(currentDate);
    }

    public static boolean isAfter(Calendar date1, Calendar date2) {
        return date2.after(date1);

    }
}
