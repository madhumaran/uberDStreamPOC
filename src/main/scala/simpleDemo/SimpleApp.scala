package simpleDemo

import java.util.Calendar

import function.functionPackage.getCurrentTime
import org.apache.spark.sql.SparkSession

object SimpleApp {

  def main(args: Array[String]) {

   //time format
   /* println(Calendar.getInstance().getTimeInMillis)
    println(getCurrentTime())*/

    //string to Long
    val lk:String="3654767 "
    println(lk.trim.toLong)
    /*println(s"Lines with a: , Lines with b: ")

    val spark = SparkSession.builder.appName("Simple Application").master("local").getOrCreate()
    val logData = spark.read.textFile(logFile).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    spark.stop()*/
  }

}
