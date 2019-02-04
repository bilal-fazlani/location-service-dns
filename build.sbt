import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "dnspoc",
    libraryDependencies += scalaTest % Test
  )


resolvers += "bintray" at "http://jcenter.bintray.com"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "com.github.mkroli" %% "dns4s-akka" % "0.11")

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
