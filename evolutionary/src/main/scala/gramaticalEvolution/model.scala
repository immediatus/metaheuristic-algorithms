package ua.org.scala
package gramaticalEvolution

object model {
  import core.Show, Show._
  import core.Navigable, Navigable._

  case class Food(x : Int, y : Int)
  case class Trail(foods : List[Food])
  case class Ant(x: Int, y : Int)

  trait Code

  case class Line(next : Code) extends Code
  case object EOC extends Code

  case class Op(next : Code) extends Line(next)
  case class Left(next : Code) extends Op(next)
  case class Right(next : Code) extends Op(next)
  case class Move(next : Code) extends Op(next)

  case class IfElse(trueBranch : Code, falseBrunch : Code) extends Code


  class Interpreter(program : Program) {
  }
}

