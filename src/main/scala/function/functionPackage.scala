package function

import java.io.File
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import domain.{Report, _}
import org.apache.spark.sql._
import hive.HiveContext
import org.apache.spark.sql.execution.streaming.state
import org.apache.spark.streaming._

import scala.collection.mutable.ListBuffer

object functionPackage {



  def getEst(currentSpeed: Int, remainingDistance: Int)= {
    val result =remainingDistance / currentSpeed
    result
  }




  //timestampHour: Long,  carNo: Long,driverName: String,  currentLocation: String,  currentSpeed: Int,  remainingDistance: Int,
  //est: Long,  city: String,  stat: String,  country: String
  //state: State[(Long, Long, String, String, Int, Int, Long, String,String,String)]
   def reportStateSpec=(k: (Long,Long),v:Option[Report],state:State[(Long,Long,String,String,Int,Int,
                                                                                    Long,String,String,String)])=> {
    println(" reportStateSpec starting")

    var(timestampHour,carNo,driverName,currentLocation,currentSpeed,remainingDistance,est,city,stat,country)=state
      .getOption().getOrElse((0L,0L,null,null,0,0,0L,null,null,null))

    val newVal = v match {
      case Some(a: Report) => (a.timestampHour,a.carNo,a.driverName,a.currentLocation,a.currentSpeed,
        a.remainingDistance,a.est,a.city,a.stat,a.country)
      case _=>(0L,0L,null,null,0,0,0L,null,null,null)
    }


   timestampHour = newVal._1
    carNo=newVal._2
    driverName=newVal._3
    currentLocation=newVal._4
   currentSpeed = newVal._5
   remainingDistance = newVal._6
    if(newVal._5>0){
      est =(newVal._6/newVal._5).toDouble.toLong
    }

    city=newVal._8
    stat= newVal._9
    country=newVal._10

    Row(timestampHour,carNo,driverName,currentLocation,currentSpeed,remainingDistance,est,city,stat,country)

  }

  /*
  * method used to get current Time
  * input: util.Date
  * output:Long value
  */
  def getCurrentTime():Long = {
    val dateFormatter = new SimpleDateFormat("YYYYMMDDHHMM")
    var submittedDateConvert = new Date()
    val submittedAt = dateFormatter.format(submittedDateConvert)
    submittedAt.toLong
  }


  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }


}
