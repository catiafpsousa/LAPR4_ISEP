package eapli.base.util;

import eapli.framework.strings.util.StringPredicates;
import eapli.framework.time.util.Calendars;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Consolle {
    private static final Logger LOGGER = LogManager.getLogger(Consolle.class);

    private Consolle() {
    }

    public static String readLine(final String prompt) {
        try {
            System.out.println(prompt);
            InputStreamReader converter = new InputStreamReader(System.in);
            BufferedReader in = new BufferedReader(converter);
            return in.readLine();
        } catch (IOException var3) {
            LOGGER.warn("Ignoring but this is really strange that it even happened.", var3);
            return "";
        }
    }

    public static String readNonEmptyLine(final String prompt, final String message) {
        while(true) {
            try {
                System.out.println(prompt);
                InputStreamReader converter = new InputStreamReader(System.in);
                BufferedReader in = new BufferedReader(converter);
                String text = in.readLine();
                if (!StringPredicates.isNullOrEmpty(text)) {
                    return text;
                }

                System.out.println(message);
            } catch (IOException var5) {
                LOGGER.warn("Ignoring but this is really strange that it even happened.", var5);
                return "";
            }
        }
    }

    public static int readInteger(final String prompt) {
        while(true) {
            try {
                String input = readLine(prompt);
                return Integer.parseInt(input);
            } catch (NumberFormatException var2) {
            }
        }
    }

    public static long readLong(final String prompt) {
        while(true) {
            try {
                String input = readLine(prompt);
                return Long.parseLong(input);
            } catch (NumberFormatException var2) {
            }
        }
    }

    public static boolean readBoolean(final String prompt) {
        while(true) {
            try {
                String strBool = readLine(prompt).toLowerCase();
                if ("y".equals(strBool) || "s".equals(strBool)) {
                    return true;
                }

                if ("n".equals(strBool)) {
                    return false;
                }
            } catch (NumberFormatException var2) {
            }
        }
    }

    public static int readOption(final int low, final int high, final int exit) {
        int option;
        do {
            option = readInteger("Select an option: ");
        } while(option != exit && (option < low || option > high));

        return option;
    }

    public static Date readDate(final String prompt) {
        return readDate(prompt, "yyyy/MM/dd");
    }

    public static Date readDate(final String prompt, final String dateFormat) {
        while(true) {
            try {
                String strDate = readLine(prompt);
                SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                return df.parse(strDate);
            } catch (ParseException var4) {
            }
        }
    }

    public static Calendar readCalendar(final String prompt) {
        return readCalendar(prompt, "dd-MM-yyyy");
    }

    public static Calendar readCalendar(final String prompt, final String dateFormat) {
        while(true) {
            try {
                String strDate = readLine(prompt);
                SimpleDateFormat df = new SimpleDateFormat(dateFormat);
                return Calendars.fromDate(df.parse(strDate));
            } catch (ParseException var4) {
            }
        }
    }

    public static double readDouble(final String prompt) {
        while(true) {
            try {
                String input = readLine(prompt);
                return Double.parseDouble(input);
            } catch (NumberFormatException var2) {
            }
        }
    }

}
