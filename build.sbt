name := "uberESTSample"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.2.0"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion ,
  "org.apache.spark" %% "spark-sql" % sparkVersion ,
  "org.apache.spark" %% "spark-mllib" % sparkVersion ,
  "org.apache.spark" %% "spark-streaming" % sparkVersion ,
  "org.apache.spark" %% "spark-hive" % sparkVersion ,
  "com.typesafe" % "config" % "1.3.1",
  "com.holdenkarau" %% "spark-testing-base" % "2.2.0_0.7.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
)
mainClass in (Compile, run) := Some("streaming.SparkStreamingMapWithState")

mainClass in (Compile, packageBin) := Some("streaming.SparkStreamingMapWithState")