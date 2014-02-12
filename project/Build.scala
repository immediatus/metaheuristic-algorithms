import sbt._
import Keys._

object Build extends sbt.Build {
  import Dependencies._

  lazy val commonSettings = Defaults.defaultSettings ++ Seq(
    organization  := "ua.org.scala",
    version       := "0.1-PoC",
    scalaVersion  := V.scala,
    scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8"),
    resolvers     ++= Dependencies.resolutionRepos
  )

  lazy val evolutionary = Project("evolutionary", file("./evolutionary"), settings = commonSettings)
}
