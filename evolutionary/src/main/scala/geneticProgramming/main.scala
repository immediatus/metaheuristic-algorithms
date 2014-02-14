package ua.org.scala
package geneticProgramming

import scala.util.{Try, Success, Failure}

object app {
  import core.Show, Show._
  import core.Navigable, Navigable._
  import core.util._

  // Genetic programm operators
  trait Operator
  object Plus extends Operator { override def toString = "+" }
  object Minus extends Operator { override def toString = "-" }
  object Div extends Operator { override def toString = "/" }
  object Mul extends Operator { override def toString = "*" }

  val operators = Seq(Plus, Minus, Div, Mul)

  // Genetic program tree
  trait Program[+T]
  case class Node[T](left : Program[T], right : Program[T], op : Operator) extends Program[T]
  case class Const[T](value: T) extends Program[T]
  case object Var extends Program[Nothing]

  // Show type-class instances
  implicit val doubleShowInstnace = new Show[Double] {
    def show = x => Math.scalb(x, 1).toString
  }

  implicit def programShowInstance[T : Show]: Show[Program[T]] = new Show[Program[T]] {
    def show = {
      case Node(l : Program[T], r : Program[T], op) => s"(${show(l)}) $op (${show(r)})"
      case Const(x) => implicitly[Show[T]].show(x)
      case Var  => "x"
    }
  }

  // Navigable type-class instances
  implicit def programNavigableInstance[T](implicit default: Program[T]): Navigable[Program[T]] = new Navigable[Program[T]] {
    def length = {
      case Node(l, r, _) => 1 + length(l) + length(r)
      case _ => 1
    }

    def iter = {
      case n @ Node(l, r, _) => n #:: iter(l) ++: iter(r)
      case term => term #:: Stream.empty[Program[T]]
    }

    def lens =
      item => {
        type Lens = Program[T] => Program[T]
        def iterate0(elem : Program[T], lens : Lens): Stream[Lens] =
           elem match {
             case n @ Node(l, r, _) =>
               (e => lens(e)) #::
              iterate0(l, e => lens(n.copy(left = e))) ++:
              iterate0(r, e => lens(n.copy(right = e)))
            case term =>
               (e => lens(e)) #:: Stream.empty[Lens]
          }
        iterate0(item, e => e)
      }

    def prune =
      item => depth => {
        def prune0(node: Program[T], d: Int): Program[T] = {
          (d, node) match {
            case (0, Node(_, _,_)) => default
            case (_, n @ Node(l, r, op)) => Node(prune0(l, d - 1), prune0(r, d - 1), op)
            case (_, elem) => elem
          }
        }
        prune0(item, depth)
      }
  }

  // Genetic program paraters & functions
  val FUNCTION_FROM = -10
  val FUNCTION_TO = 10

  implicit val default: Program[Double] = randomTerm

  def targetFunction: Double => Double =
    x => 2 * x * x - 3 * x - 4

  def randomInput: Double =
    util.Random.nextDouble * (FUNCTION_TO - FUNCTION_FROM) + FUNCTION_FROM

  def randomOperator: Operator =
    operators(util.Random.nextInt(operators.length))

  def randomTerm: Program[Double] =
    if(util.Random.nextBoolean) Const(util.Random.nextDouble * 20 - 10)
    else Var

  def randomProgramm(depth : => Int): Program[Double] = {
    import util.Random._, Math._
    if(depth == 0 || nextDouble < 0.1) randomTerm
    else Node(randomProgramm(depth - 1), randomProgramm(depth - 1), randomOperator)
  }

  def eval: Program[Double] => Double => Try[Double] =
     program => x => Try {
        def eval0(p : Program[Double]): Double = {
           p match {
              case Node(l, r, Plus)   => eval0(l) + eval0(r)
              case Node(l, r, Minus)  => eval0(l) - eval0(r)
              case Node(l, r, Div)    => eval0(l) / eval0(r)
              case Node(l, r, Mul)    => eval0(l) * eval0(r)
              case Const(value)       => value
              case Var                => x
           }
        }
        eval0(program)
     }

  def segmentError(programMap : => List[(Double, Double)]): Program[Double] => Double =
     program => {
       val programF = eval(program)
       programMap.map {
         case (x, fx) => programF(x).map { e => Math.abs(e - fx) }
       }.sequece match {
         case Success(xs) => xs.sum / programMap.length
         case Failure(_)  => Double.MaxValue
       }
     }

  def crossover[T](rate : => Double, maxDepth : => Int)(implicit default: Program[T]): (Program[T], Program[T]) => Program[T] =
    (parentA, parentB) => {
      import util.Random._
      if(nextDouble >= rate) parentA
      else parentA.lens(nextInt(parentA.length)) apply (parentB.iter(nextInt(parentB.length))).prune(maxDepth)
    }

  def mutation[T](
    rate : => Double,
    maxDepth : => Int,
    random : => Program[T]
  )(implicit default: Program[T]):  Program[T] => Program[T] =
    program => {
      import util.Random._

      @annotation.tailrec
      def mutation0(times : Int, prg : Program[T]): Program[T] =
        if(times == 0) prg
        else mutation0(times - 1, prg.lens(nextInt(prg.length)) apply random)

      mutation0(
        program.length.times { i => if(nextDouble < rate) 1 else 0 }.sum,
        program)
      .prune(maxDepth)
    }


  def main(args : Array[String]) {
    import core._


    val testSetF    = (x : Double) => (x, targetFunction(x))
    val MAX_DEPTH   = 7
    val crossoverF  = crossover[Double](0.90, MAX_DEPTH)
    val mutationF   = mutation(0.015, MAX_DEPTH, randomProgramm(MAX_DEPTH))
    val fitnessF    = segmentError(30.times { _ => testSetF(randomInput) }.toList)
    val tournamentF = genetic.tournament[Program[Double]]
    val searchF     = genetic.search[Program[Double]](crossoverF, mutationF, fitnessF, tournamentF, 100, 100)
    val population  = List.fill(100) { randomProgramm(MAX_DEPTH) }

    val best = searchF(population)
    println(best.show)

  }
}

