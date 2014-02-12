import sbt._

object Dependencies {
  val resolutionRepos = Seq(
    "Typesafe repo" at "http://repo.typesafe.com/typesafe/releases/"
  )

  def compile   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "compile")
  def provided  (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "provided")
  def test      (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "test")
  def runtime   (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "runtime")
  def container (deps: ModuleID*): Seq[ModuleID] = deps map (_ % "container")

  object V {
    val scala         = "2.10.3"
    val akka          = "2.2.3"
  }

  val akka            = "com.typesafe.akka"       %%  "akka-actor"        % V.akka
}
