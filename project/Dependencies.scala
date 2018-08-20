import sbt._

object Dependencies {

  val playWSStandaloneVersion = "2.0.0-M3"

  val playAhcWSStandalone = "com.typesafe.play" %% "play-ahc-ws-standalone" % playWSStandaloneVersion
  val playJson = "com.typesafe.play" %% "play-ws-standalone-json" % playWSStandaloneVersion
  val fakePlayWSStandalone = "org.f100ded.play" % "play-fake-ws-standalone_2.12" % "1.1.0" % Test
  val playWS = "com.typesafe.play" %% "play-ws" % "2.7.0-M2"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5-M1" % Test

}
