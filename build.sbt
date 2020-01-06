name := """scala-cta-demo"""

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.github.tototoshi" %% "scala-csv" % "1.2.2",
  "joda-time" % "joda-time" % "2.8.2")
