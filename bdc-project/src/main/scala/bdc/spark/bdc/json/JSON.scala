package bdc.spark.bdc.json

import net.minidev.json.{JSONObject, JSONValue}

/**
 * Created by mikio on 24.02.15.
 */
class JSON(json: Any) {
  val j = json.asInstanceOf[JSONObject]

  def get(key: String): JSON = json.get
}

object JSON {
  def parse(s: String) = new JSON(JSONValue.parse(s))
}
