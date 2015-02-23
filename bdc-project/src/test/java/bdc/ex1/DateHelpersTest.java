package bdc.ex1;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;

public class DateHelpersTest {

    SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.ENGLISH);

    @Test
    public void floorToHour() throws Exception {
        Date date = DATE_FORMAT.parse("2014.01.01 12:10:15");
        Date res = DateHelpers.floorToHour(date);
        assertEquals("2014.01.01 12:00:00", DATE_FORMAT.format(res));
    }

    @Test
    public void floorToMidnight() throws Exception {
        Date date = DATE_FORMAT.parse("2014.01.01 12:10:15");
        Date res = DateHelpers.floorToMidnight(date);
        assertEquals("2014.01.01 00:00:00", DATE_FORMAT.format(res));
    }

    @Test
    public void floorToMonday_fromWednesday() throws Exception {
        Date date = DATE_FORMAT.parse("2015.02.25 12:10:15");
        Date res = DateHelpers.floorToMonday(date);
        assertEquals("2015.02.23 00:00:00", DATE_FORMAT.format(res));
    }

    @Test
    public void floorToMonday_fromSaturday() throws Exception {
        Date date = DATE_FORMAT.parse("2015.02.28 12:10:15");
        Date res = DateHelpers.floorToMonday(date);
        assertEquals("2015.02.23 00:00:00", DATE_FORMAT.format(res));
    }

    @Test
    public void floorToMonth() throws Exception {
        Date date = DATE_FORMAT.parse("2015.02.28 12:10:15");
        Date res = DateHelpers.floorToMonth(date);
        assertEquals("2015.02.01 00:00:00", DATE_FORMAT.format(res));
    }
}
