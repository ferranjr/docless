name := "swala"

version := "1.0"

val circeVersion = "0.5.4"
val enumeratumVersion = "1.5.1"

val commonSettings = Seq(
  scalaVersion := "2.11.8",
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "com.beachape" %% "enumeratum" % enumeratumVersion,
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )
)

//val paradise = "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full

lazy val macros = project.in(file("macros")).settings(commonSettings : _*)
lazy val swala = project.in(file("swala")).settings(commonSettings : _*).dependsOn(macros)
