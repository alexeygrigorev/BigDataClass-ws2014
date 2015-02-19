package bdc.jsonexamples;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

/**
 * Example how to parse JSONs in Java
 */
public class JSONParsing {
  private static String json = "{ \"number\": 1, \"list\": [1,2,3], \"object\": {\"a\": 1, \"b\": \"c\"}}";

  public static void main(String[] args) {
    JSONObject j = (JSONObject)JSONValue.parse(json);

    // So what did we get?
    println(j);
    println(j.getClass().getName());

    // getting an entry as a number
    printf("number = %d\n", j.getAsNumber("number"));

    // getting a list
    printf("list = %s\n", (JSONArray) j.get("list"));
    JSONArray a = (JSONArray)j.get("list");
    for (int i = 0; i < a.size(); i++) {
      printf("  entry %d = %s of class %s\n", i, a.get(i), a.get(i).getClass().getName());
    }

    // Hm. What happens if we pull the wrong type?
    try {
      j.getAsNumber("list");
    } catch(ClassCastException cce) {
      println("Well, that didn't work, obviously...");
    }

    // How to check what we have. This is rather dirty, but well...
    if (!(j.get("object") instanceof JSONArray)) {
      println("Doesn't look like it...");
    }

    // Finally, objects
    JSONObject obj = (JSONObject) j.get("object");
    println("object contains keys " + obj.keySet());
    printf("object[\"a\"] = %d\n", obj.getAsNumber("a"));
    printf("object[\"b\"] = %s\n", obj.getAsString("b"));

    // Non-existant?
    printf("object has key \"c\" => %s?\n", obj.keySet().contains("c"));

    // And if we still try to get it?
    printf("object[\"c\"] = %s\n", obj.get("c"));

    // Fair enough...
  }

  public static void printf(String fmt, Object... args) {
    System.out.printf(fmt, args);
  }

  public static void println(Object obj) {
    System.out.println(obj.toString());
  }
}

