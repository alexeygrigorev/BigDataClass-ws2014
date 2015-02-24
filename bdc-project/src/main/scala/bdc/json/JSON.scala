package bdc.json

import java.text.SimpleDateFormat
import java.util.{TimeZone, Locale, Date}

import net.minidev.json.{JSONArray, JSONObject, JSONValue}

import scala.collection.mutable

/**
 * Minimal JSON helper functions
 */
class JSON(json: Any) {
  val j = json

  def asInt = j.asInstanceOf[Int]

  def asLong = j.asInstanceOf[Long]

  def asObject: JSONObject = j.asInstanceOf[JSONObject]

  def asString: String = j.asInstanceOf[String]

  def asArray: Vector[JSON] = {
    val a = j.asInstanceOf[JSONArray]
    val b = mutable.ArrayBuffer.empty[JSON]
    for (i <- 0 until a.size) {
      b += new JSON(a.get(i))
    }
    b.toVector
  }

  def asDate: Date = {
    val fmt = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", new Locale("en"))
    fmt.setTimeZone(TimeZone.getTimeZone("UTC"))
    fmt.parse(asString)
  }

  override def toString = j.toString

  def get(key: String): JSON = {
    if (asObject.containsKey(key))
      new JSON(asObject.get(key))
    else
      throw new NoSuchElementException
  }

  def getString(key: String): String = asObject.getAsString(key)

  def getInt(key: String): Int = get(key).asInt

  def getLong(key: String): Long = get(key).asLong

  def getDate(key: String): Date = get(key).asDate

  def getArray(key: String): Vector[JSON] = get(key).asArray
}

object JSON {
  def parse(s: String) = new JSON(JSONValue.parse(s))
}
