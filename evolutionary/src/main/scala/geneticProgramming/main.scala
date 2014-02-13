package ua.org.scala
package geneticProgramming

object app {
  import core.Show, Show._

  trait Operator
  object Plus extends Operator { override def toString = "+" }
  object Minus extends Operator { override def toString = "-" }
  object Div extends Operator { override def toString = "/" }
  object Mul extends Operator { override def toString = "*" }

  val operators = Seq(Plus, Minus, Div, Mul)

  trait Program[+T]
  case class Node[T](left : Program[T], right : Program[T], op : Operator) extends Program[T]
  case class Number[T](value: T) extends Program[T]
  case object Variable extends Program[Nothing]

  // Show type-class instances
  implicit val doubleShowInstnace = new Show[Double] {
    def show = _.toString
  }

  implicit def programShowInstance[T : Show]: Show[Program[T]] = new Show[Program[T]] {
    def show = {
      case Node(l : Program[T], r : Program[T], op) => s"(${show(l)}) $op (${show(r)})"
      case Number(x) => implicitly[Show[T]].show(x)
      case Variable  => "x"
    }
  }

  val FUNCTION_FROM = -10
  val FUNCTION_TO = 10

  def targetFunction: Double => Double =
    x => 2 * x * x - 3 * x - 4

  def randomInput: Double =
    util.Random.nextDouble * (FUNCTION_TO - FUNCTION_FROM) + FUNCTION_FROM

  def randomOperator: Operator =
    operators(util.Random.nextInt(operators.length))

  def randomTerm: Program[Double] =
    if(util.Random.nextBoolean) Number(util.Random.nextDouble * 20 - 10)
    else Variable

  def randomProgramm(depth : Int): Program[Double] = {
    import util.Random._, Math._
    if(depth == 0 || nextDouble < 0.1) randomTerm
    else Node(randomProgramm(depth - 1), randomProgramm(depth - 1), randomOperator)
  }

  def eval: Program[Double] => Double => Double =
     program => x => {
        def eval0(p : Program[Double]): Double = {
           p match {
              case Node(l, r, Plus)    => eval0(l) + eval0(r)
              case Node(l, r, Minus)   => eval0(l) - eval0(r)
              case Node(l, r, Div)     => eval0(l) / eval0(r)
              case Node(l, r, Mul)     => eval0(l) * eval0(r)
              case Number(value)       => value
              case Variable            => x
           }
        }
        eval0(program)
     }

  def segmentError(programMap : List[(Double, Double)]): (Double => Double) => Double =
     program => {
       programMap.map {
         case (x, fx) => Math.abs(program(x) - fx)
       }.sum / programMap.length
     }


  def main(args : Array[String]) {
    val program = randomProgramm(5)
    println(program.show)
  }
}

