package streaming

import domain._
import function.functionPackage._
import org.apache.spark.SparkContext
import org.apache.spark.streaming._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import sparkUtils.SparkUtil._
import sparkUtils.schemaUtil._

object SparkStreamingMapWithState extends App{

  val sc=getSparkContext("SparkStreaming")

  val sqlContext = getSparkSqlContext(sc)
  import sqlContext.implicits._

  //initialize RDD
  val sourceFile = "file:///home/jpasolutions/spark/cloneOfKafkaProjects/" +
    "spark-kafka-cassandra-applying-lambda-architecture/unProcessed/"

  val reportSchema = getReportSchema()
  val batchDuration=Seconds(60)
  val reportDetailStateSpec = StateSpec.function(reportStateSpec).timeout(Seconds(100))

  /*    method streamingApp
           */
    def uberStreamingApp(sc: SparkContext, batchDuration: Duration)= {
      val ssc = new StreamingContext(sc, batchDuration)

      println("filename-->" + s"$sourceFile")
      val reader = ssc.textFileStream(sourceFile)

      //rdd Transformation
      val finalStateReport = reader.transform(rdd1 =>{
       rdd1.flatMap { line =>
         val splittedRecord = line.split(",")
         if (splittedRecord.length >= 12)
           Some(domain.CarDetail(getCurrentTime(), splittedRecord(0).trim.toLong,
             splittedRecord(1).trim.toInt, splittedRecord(2), splittedRecord(3).trim.toLong, splittedRecord(4),
             splittedRecord(5).trim.toLong, splittedRecord(6), splittedRecord(7).trim.toInt, splittedRecord(8).trim.toInt,
             splittedRecord(9), splittedRecord(10), splittedRecord(11)))
         else
           None
       }.toDF().createOrReplaceTempView("activity")
        val reportByCarDetails = sqlContext.sql(
          """ SELECT timestampHour,carNo,driverName,currentLocation,currentSpeed,remainingDistance,
            |city,stat,country from activity """.stripMargin).rdd
        println("second------------------->"+reportByCarDetails.collect().foreach(println))
        reportByCarDetails.map{
          a => ((a.getLong(0),a.getLong(1)),Report(a.getLong(0),a.getLong(1),a.getString(2),a.getString(3),
            a.getInt(4),a.getInt(5),0L,a.getString(6),a.getString(7),a.getString(8))
          )}
      }).mapWithState(reportDetailStateSpec)

      finalStateReport.foreachRDD(stateRdd => {
        if (!stateRdd.isEmpty()) {
          lazy val hqlcontext = new org.apache.spark.sql.hive.HiveContext(stateRdd.sparkContext)
          hqlcontext.sql("SET hive.exec.dynamic.partition = true;")
          hqlcontext.sql("SET hive.exec.dynamic.partition.mode = nonstrict;")
          println("stateDataFrame Staert------------------->")
          println(stateRdd.collect().foreach(println))
          println("stateDataFrame End------------------------>")

          val stateDataFrame1 = hqlcontext.createDataFrame(stateRdd, new Reports().schema)

            stateDataFrame1.createOrReplaceTempView("my_temp_table")
          hqlcontext.sql("create database uber")
          hqlcontext.sql("use uber")
          hqlcontext.sql("create table IF NOT EXISTS uber.report USING " +
              s"org.apache.spark.sql.parquet PARTITIONED BY (country,stat,city,currentLocation) " +
              s" as select * from my_temp_table")
          hqlcontext.sql("insert into table uber.report select * from my_temp_table")
            /*val tablepath = Map("path" -> "hdfs://localhost:8020/user/hive/warehouse")
            smTrgPart.write.format("parquet").partitionBy("country", "stat", "city", "currentLocation"
            ).options(tablepath).mode(SaveMode.Append).saveAsTable("report")*/
            println("start Result------------------->")
            println(hqlcontext.sql("select * from uber.report"))
            println("end ------------------------>")
          }
      })
       ssc
    }

  val ssc = getStreamingContext(uberStreamingApp,sc,batchDuration)
  ssc.checkpoint("uber/checkPointDir")
  ssc.start()
  ssc.awaitTermination()
}
