package bdc.jsonexamples;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class to demonstrate date parsing in Java
 */
public class JSONDateParsing {
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

  // need to set the time zone, too!
  static {
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
  }

  public static void main(String[] args) {
    JSONObject tweet = (JSONObject) JSONValue.parse(ExampleTweet.tweet);

    String created_at = tweet.getAsString("created_at");

    System.out.println("Created at = " + created_at);

    // now let's parse this!
    try {
      Date createdAt = dateFormat.parse(created_at);

      // We can also use the format to reconstruct the string
      System.out.println("Projected back: " + dateFormat.format(createdAt));
    } catch (java.text.ParseException pe) {
      System.out.println("Parse error!");
    }
  }
}
