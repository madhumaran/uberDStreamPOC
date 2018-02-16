package hive

import org.apache.spark.SparkContext
import org.apache.spark.sql.hive.HiveContext

object HiveContext {
  @transient private var instance: HiveContext = _
    def getInstance(sparkContext: SparkContext): HiveContext = {
      synchronized {
        if (instance == null ) {
          instance = new HiveContext(sparkContext)
        }
        instance
      }
    }

}
