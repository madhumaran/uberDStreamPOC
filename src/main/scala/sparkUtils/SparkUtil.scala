package sparkUtils

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}


object SparkUtil {

  val hiveLocation="hdfs://localhost:8020/user/hive/warehouse"
  def getSparkContext(apiName:String)={
    val config=new SparkConf().setAppName(apiName).setMaster("local[*]").set("spark.sql.warehouse.dir",hiveLocation)
      .set("spark.sql.sources.maxConcurrentWrites","1")
      .set("spark.sql.parquet.compression.codec", "snappy")
      .set("hive.exec.dynamic.partition", "true")
      .set("hive.exec.dynamic.partition.mode", "nonstrict")
      .set("parquet.compression", "SNAPPY")
      .set("hive.exec.max.dynamic.partitions", "3000")
      .set("parquet.enable.dictionary", "false")
      .set("hive.support.concurrency", "true")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val sc=new SparkContext(config)
    sc
  }

  def getSparkConfig(apiName:String)={
    val config=new SparkConf().setAppName(apiName).setMaster("local[*]")
    config
  }

  def getSparkSqlContext(sc:SparkContext)={
    val sqlContex = SQLContext.getOrCreate(sc)
    sqlContex
  }

  def getSparkSession(apiName:String) ={
    val sparkSession = SparkSession.builder().appName(apiName)
      .enableHiveSupport()
      .config("hive.exec.dynamic.partition", "true")
      .config("hive.exec.dynamic.partition.mode", "nonstrict")
      .master("local(*)").getOrCreate()
    sparkSession
  }

  //recreating the streaming context from previously saved check point if available
  //function streamingApp: (SparkContext,Duration)  , returns => StreamingContext
  // why? get active streaming context if exists from checkpoint directory not get create new
  def getStreamingContext(streamingApp: (SparkContext,Duration) => StreamingContext,sc: SparkContext,batchDuration: Duration)={
    val creatingFunc = () => streamingApp(sc, batchDuration)
    val ssc = sc.getCheckpointDir match {
      case Some(checkpointDir) => StreamingContext.getActiveOrCreate(checkpointDir,creatingFunc,sc.hadoopConfiguration,createOnError = true)
      case None => StreamingContext.getActiveOrCreate(creatingFunc)
    }
    sc.getCheckpointDir.foreach(cp => ssc.checkpoint(cp))
    ssc
  }





}
