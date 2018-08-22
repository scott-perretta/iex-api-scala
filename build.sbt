import sbt._
import Dependencies._

scalaVersion := "2.13.0-M3"

crossScalaVersions := Seq("2.11.12", "2.12.6")

organization := "perretta"

lazy val root = (project in file("."))
  .settings(
    name := "iex-api-scala",
    libraryDependencies ++= Seq(
      fakePlayWSStandalone,
      playAhcWSStandalone,
      playJson,
      playJsonJoda,
      playWS,
      scalaTest
    )
  )
