import sbt._
import Dependencies._

lazy val root = (project in file("."))
  .settings(
    name := "iex-api-scala",
    organization := "perretta",
    scalaVersion := "2.13.0-M3",
    libraryDependencies ++= Seq(
      playWs,
      scalaTest
    )
  )

