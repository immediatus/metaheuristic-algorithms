package ua.org.scala
package gramaticalEvolution

object model {
  import core.Show, Show._
  import core.Navigable, Navigable._

  case class Food(x : Int, y : Int)
  case class Trail(foods : List[Food])
  case class Ant(x: Int, y : Int)

  trait Expr
  case object EOE extends Expr

  abstract class Op(next : Expr) extends Expr
  case class Left(next : Expr) extends Op(next)
  case class Right(next : Expr) extends Op(next)
  case class Move(next : Expr) extends Op(next)

  case class IfFoodAhead(trueNext : Expr, falseNext : Expr) extends Expr

  class Interpreter(expression : Expr) {
  }
}

