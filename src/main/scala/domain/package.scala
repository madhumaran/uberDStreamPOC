
/**
  * domain --> rc-->new --> packageObject
  */
package object domain {

  case class CarDetail(timestampHour: Long,
                       carNo: Long,
                       currentSpeed: Int,
                       direction: String,
                       imeiNo: Long,
                       currentLocation: String,
                       driverId: Long,
                       driverName: String,
                       totDistance: Int,
                       remainingDistance: Int,
                       city: String,
                       stat: String,
                       country: String)extends Serializable

  case class Report(timestampHour: Long,
                    carNo: Long,
                    driverName: String,
                    currentLocation: String,
                    currentSpeed: Int,
                    remainingDistance: Int,
                    est: Long,
                    city: String,
                    stat: String,
                    country: String)extends Serializable



}
