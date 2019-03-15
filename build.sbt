name := """play-java-starter-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.12.7"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies ++= Seq(guice, jdbc, ws, evolutions)
libraryDependencies += javaJdbc % Test

PlayKeys.devSettings += "play.server.http.address" -> "localhost"

// Test Database
libraryDependencies += "com.h2database" % "h2" % "1.4.197"
libraryDependencies += "io.ebean" % "ebean" % "11.28.3"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"

// Testing libraries for dealing with CompletionStage...
libraryDependencies += "org.assertj" % "assertj-core" % "3.6.2" % Test
libraryDependencies += "org.awaitility" % "awaitility" % "2.0.0" % Test

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))