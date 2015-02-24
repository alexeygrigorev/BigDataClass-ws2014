package bdc.json

import bdc.jsonexamples.ExampleTweet
import org.junit._
import Assert._

/**
 * Testing JSON implementation.
 */
class TestJSON {
  @Test
  def simpleTest(): Unit = {
    val j = JSON.parse(ExampleTweet.tweet)

    assertEquals("RT @daniandreajonas: RT @DisneyStarFacts: “Cuando Nick está por caerse, hace todo lo posible por evitarlo… (cont) http://tl.gd/91l9ba", j.getString("text"))
    assertEquals(42011092947312640L, j.getLong("id"))
    assertEquals("ladie131994", j.get("user").getString("screen_name"))
    assertEquals("{\"location\":\"Jonasland-vzla\",\"statuses_count\":9847,\"profile_background_tile\":true,\"lang\":\"es\",\"profile_link_color\":\"b608f0\",\"id\":65084381,\"following\":null,\"favourites_count\":3,\"protected\":false,\"profile_text_color\":\"ff0894\",\"verified\":false,\"contributors_enabled\":false,\"description\":\"I'm so friendly and funny..I LOVE JONAS BROTHERS and DEMI LOVATO too..I LOVE to speak in english instead spanish..my favorite JONAS is JOE JONAS I♥jb and demii \",\"name\":\"maria jonas miller\",\"profile_sidebar_border_color\":\"6d0c94\",\"profile_background_color\":\"dbbad7\",\"created_at\":\"Wed Aug 12 16:39:20 +0000 2009\",\"followers_count\":863,\"geo_enabled\":false,\"profile_background_image_url\":\"http:\\/\\/a2.twimg.com\\/profile_background_images\\/138891567\\/60594944.jpg\",\"follow_request_sent\":null,\"url\":\"http:\\/\\/twitter.com\\/joejonas4ever\",\"utc_offset\":-16200,\"time_zone\":\"Caracas\",\"notifications\":null,\"profile_use_background_image\":true,\"friends_count\":2001,\"profile_sidebar_fill_color\":\"000000\",\"screen_name\":\"ladie131994\",\"id_str\":\"65084381\",\"show_all_inline_media\":false,\"profile_image_url\":\"http:\\/\\/a1.twimg.com\\/profile_images\\/1212115264\\/169020_119926581409275_100001758021970_111399_2406583_n_normal.jpg\",\"listed_count\":42,\"is_translator\":false}",
      j.get("user").toString)
    var i = 0
    val names = Vector("Dani Andrea ", "DisneyStarsFacts ©")
    for (m <- j.get("entities").get("user_mentions").asArray) {
      assertEquals(names(i), m.getString("name"))
      i += 1
    }

    assertEquals(1298851199000L, j.getDate("created_at").getTime)
  }
}
