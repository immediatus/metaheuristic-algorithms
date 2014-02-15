package ua.org.scala
package geneticProgramming

object model {
  import core.Show, Show._
  import core.Navigable, Navigable._

  // Genetic program operators
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
    def show = _.toString
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
}

