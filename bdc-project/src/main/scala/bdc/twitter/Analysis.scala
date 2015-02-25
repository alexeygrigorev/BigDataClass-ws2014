package bdc.twitter

import bdc.json.JSON
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext._

/**
 * <one line description>
 *
 * <longer description>
 *
 * User: mikio
 * Date: 2/24/15
 * Time: 10:15 PM
 */
class Analysis {
  private val conf = new SparkConf().setAppName("wordCount")
  private val sc = new SparkContext(conf)

  def loadTweets(path: String): RDD[JSON] = null

  def filterTweets(user: Seq[String], tweets: RDD[JSON]): RDD[JSON] = null

  def computeTrend(objects: RDD[String]): RDD[(String, Int)] = null
}

object Analysis {
  def main(args: Array[String]) {
    val a = new Analysis
    val tweets = a.loadTweets(args(0))

    // ...
  }
}