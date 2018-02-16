package sparkUtils

import org.apache.spark.sql.Encoders

object schemaUtil {

  def getReportSchema()={
    val schema=Encoders.product[domain.Report].schema
    schema
  }

  def getCarSchema()={
    Encoders.product[domain.CarDetail].schema
  }

}
