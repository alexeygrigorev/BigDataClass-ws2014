package bdc.spark

import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.SparkContext._

/**
 * Simple standalone Spark job
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("wordCount")
    val sc = new SparkContext(conf)

    val file = sc.textFile(args(0))

    val words = file.flatMap(l => l.split(" "))

    val counts = words.map(w => (w, 1)).reduceByKey((x,y) => x+y)

    println("Done " + counts.collect())

    counts.saveAsTextFile(args(1))
  }
}
