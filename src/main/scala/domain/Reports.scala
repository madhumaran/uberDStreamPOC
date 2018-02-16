package domain

import org.apache.spark.sql._
import org.apache.spark.sql.types.{StructField, _}

class Reports extends Serializable {
 private var timestampHour: Long=0L
  private var carNo: Long=0L
  private var driverName: String=""
  private var currentLocation: String=""
  private var currentSpeed: Int=0
  private var remainingDistance: Int=0
  private var est: Long=0L
  private var city: String=""
  private var stat: String=""
  private var country: String=""

  def schema:StructType = (new StructType).add("timestampHour", LongType)
    .add("carNo", LongType)
    .add("driverName",StringType)
    .add("currentLocation", StringType)
    .add("currentSpeed",IntegerType)
    .add("remainingDistance",IntegerType)
    .add("est",LongType)
    .add("city",StringType)
    .add("stat",StringType)
    .add("country",StringType)

}
