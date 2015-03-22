import com.typesafe.sbt.gzip.Import.gzip
import com.typesafe.sbt.web.SbtWeb
import play.PlayImport._
import play.PlayScala

name := """Yollo-Octo-Ninja"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala,SbtWeb)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  filters,
  cache,
  ws,
  "org.webjars" % "jquery" % "2.1.1"
)

PlayKeys.playDefaultPort := 10000


scalacOptions in ThisBuild ++= Seq(
  "-target:jvm-1.7",  // todo upgrade to 1.8
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-language:postfixOps"
)


//libraryDependencies ++= Seq(
//  "org.webjars" % "bootstrap" % "3.3.2" exclude("org.webjars", "jquery"),
//  "org.webjars" % "jquery" % "2.1.1"
//  "org.webjars" % "lodash" % "2.4.1-6"
//)

