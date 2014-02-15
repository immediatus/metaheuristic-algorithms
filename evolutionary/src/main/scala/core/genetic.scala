package ua.org.scala
package core

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
          val next = reproduce0 {
              fitPopulation { tournament(population) }
          }.sortBy { Memo(fitness) }.take(popSize)
          search0(gen - 1, next, List(next.head, best).minBy { fitness })
        }
      }

      search0(maxGens, population, population minBy fitness)
    }
}
