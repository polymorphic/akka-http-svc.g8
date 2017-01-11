import sbt._
import sbt.Keys._

lazy val rootDependencies = {
  Seq(
    "de.heikoseeberger" %% "akka-http-circe" % "$akkaHttpCirceVersion$"
    , "com.typesafe.akka" %% "akka-http" % "$akkaHttpVersion$"
    , "io.circe" %% "circe-core" % "$circeVersion$"
    , "io.circe" %% "circe-generic" % "$circeVersion$"
    , "io.circe" %% "circe-parser" % "$circeVersion$"
    , "com.typesafe.akka" %% "akka-slf4j" % "$akkaVersion$"
    , "ch.qos.logback" % "logback-classic" % "1.1.8"
  )
}

lazy val testDependencies = {
  Seq(
    "com.typesafe.akka" %% "akka-http-testkit" % "$akkaHttpVersion$" % "test"
  )
}

lazy val dependencies = rootDependencies ++ testDependencies

lazy val compileSettings = Seq(
  "-deprecation"
  ,"-target:jvm-1.8"
)

lazy val forkedJvmOption = Seq(
  "-server"
)

lazy val settings = Seq(
  name := "$name$"
  , organization := "$organization$"
  , version := "0.0.1-SNAPSHOT"
  , scalaVersion := "$scalaversion$"
  , libraryDependencies ++= dependencies
  , fork in run := true
  , fork in Test := true
  , fork in testOnly := true
  , connectInput in run := true
  , javaOptions in run ++= forkedJvmOption
  , javaOptions in Test ++= forkedJvmOption
  , scalacOptions := compileSettings
)

lazy val buildinfoSettings = Seq(
  buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
  , buildInfoOptions += BuildInfoOption.ToJson
)

dockerfile in docker := {
  val appDir: File = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    entryPoint(s"\$targetDir/bin/\${executableScriptName.value}")
    copy(appDir, targetDir)
    expose(8080)
  }
}

val main =
  project
    .in(file("."))
    .enablePlugins(BuildInfoPlugin,JavaAppPackaging,sbtdocker.DockerPlugin)
    .settings(buildinfoSettings ++ settings: _*)
