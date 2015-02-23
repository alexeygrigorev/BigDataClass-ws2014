package bdc.ex1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Helper functions to round down dates to specific granularities.
 */
public class DateHelpers {

    private static final SimpleDateFormat TWITTER_DATE_FORMAT = new SimpleDateFormat(
            "EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

    static {
        TWITTER_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static Date parse(String date) {
        try {
            return TWITTER_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("date is not parsable, got: " + date, e);
        }
    }

    public static Date floorToHour(Date date) {
        Calendar calendar = toCalendar(date);
        floorToHour(calendar);
        return calendar.getTime();
    }

    private static void floorToHour(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
    }

    private static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Date floorToMidnight(Date date) {
        Calendar calendar = toCalendar(date);
        floorToMidnight(calendar);
        return calendar.getTime();
    }

    private static void floorToMidnight(Calendar calendar) {
        floorToHour(calendar);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }

    public static Date floorToMonth(Date date) {
        Calendar calendar = toCalendar(date);
        floorToMonth(calendar);
        return calendar.getTime();
    }

    private static void floorToMonth(Calendar calendar) {
        floorToMidnight(calendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    public static Date floorToMonday(Date date) {
        Calendar calendar = toCalendar(date);
        floorToMonday(calendar);
        return calendar.getTime();
    }

    private static void floorToMonday(Calendar calendar) {
        floorToMidnight(calendar);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    }
}
