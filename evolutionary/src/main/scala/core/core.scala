package ua.org.scala
package core



trait Show[T] { def show: T => String }

object Show {
  implicit def showExtension[T : Show](value : T) = new {
    def show: String = implicitly[Show[T]].show(value)
  }
}


trait Navigable[T] {
  def length: T => Int
  def iter:   T => Stream[T]
  def lens:   T => Stream[T => T]
  def prune:  T => Int => T
}

object Navigable {
  implicit def toNavigable[T : Navigable](obj : T) = new {
    def length: Int = implicitly[Navigable[T]].length(obj)
    def iter: Stream[T] = implicitly[Navigable[T]].iter(obj)
    def lens: Stream[T => T] = implicitly[Navigable[T]].lens(obj)
    def prune: Int => T = implicitly[Navigable[T]].prune(obj)
  }
}


object util {
  implicit def timesExt(repeat : Int) = new {
    def times[T](f: Int => T): Seq[T] = {
      def times0(i : Int) : List[T] = if(i < repeat) f(i) :: times0(i + 1) else Nil
      times0(0)
    }
  }

  import scala.util.{Try, Success, Failure}
  implicit def sequenceExt[T](xs : List[Try[T]]) = new {
    def sequece: Try[List[T]] = {
      val (ss: List[Success[T]]@unchecked, fs: List[Failure[T]]@unchecked) =
      xs.partition(_.isSuccess)

      if (fs.isEmpty) Success(ss map (_.get))
      else Failure[List[T]](fs(0).exception)
    }
  }
}

object genetic {

  def tournament[T]: List[T] => List[T] =
    population => {
      import scala.util.Random._
      val length = population.length - 1
      population.map { _ => population(nextInt(length)) }
    }

  def search[T](
    crossover:  (T, T) => T,
    mutation:   T => T,
    fitness:    T => Double,
    tournament: List[T] => List[T],
    maxGens:    Int,
    popSize:    Int
  ): List[T] => T =
    population => {

      def fitPopulation(population: List[T]): List[T] =
        if(population.length % 2 == 0) population else population :+ population.head

      def reproduce0(parent : List[T]): List[T] = {
        parent match {
          case a :: b :: tail =>
            mutation(crossover(a, b)) ::
            mutation(crossover(b, a)) ::
            reproduce0(tail)
          case _ => Nil
        }
      }

      @annotation.tailrec
      def search0(gen : Int, population : List[T], best: T): T = {
        if(gen == 0) best
        else {
          val next = reproduce0(
            fitPopulation(
              tournament(population))
          ).sortBy { fitness }.take(popSize)
          search0(gen - 1, next, List(next.head, best).minBy { fitness })
        }
      }

      search0(maxGens, population, population minBy fitness)
    }
}
