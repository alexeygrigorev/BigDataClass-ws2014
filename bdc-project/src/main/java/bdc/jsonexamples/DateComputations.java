package bdc.jsonexamples;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class to demonstrate date computations in Java.
 */
public class DateComputations {
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

    // need to set the time zone, too!
    static {
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
      JSONObject tweet = (JSONObject) JSONValue.parse(ExampleTweet.tweet);

      // now let's parse this!
      try {
        Date createdAt = dateFormat.parse(tweet.getAsString("created_at"));

        System.out.println("original time: " + dateFormat.format(createdAt));

        // First, we store the Date into a Calendar object
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Calendar c = Calendar.getInstance(tz);
        c.setTime(createdAt);

        // Now we can, for example, set the minutes & seconds to zero
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);

        System.out.println("Result, rounded down to hour = " + dateFormat.format(c.getTime()));

        // Now let's add 87 minutes
        c.add(Calendar.SECOND, 33);
        c.add(Calendar.MINUTE, 87);
        System.out.println("Result, rounded down to hour plus 87 minutes, 33 seconds = " + dateFormat.format(c.getTime()));
      } catch (java.text.ParseException pe) {
        System.out.println("Parse error!");
      }
    }
}
