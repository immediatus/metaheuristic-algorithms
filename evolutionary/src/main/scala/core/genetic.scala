package ua.org.scala
package core

object genetic {

  def search[T](
    replacement:  (T, T) => T,
    mutation:     T => T,
    fitness:      T => Double,
    selection:    List[T] => List[T],
    maxGens:      Int,
    popSize:      Int
  ): List[T] => T =
    population => {

      def padPopulation: List[T] => List[T] =
        population => if(population.length % 2 == 0) population else population :+ population.head

      def reproduce0: List[T] => List[T] = {
          case a :: b :: tail =>
            mutation(replacement(a, b)) ::
            mutation(replacement(b, a)) ::
            reproduce0(tail)
          case _ => Nil
      }

      def evolutionOperator: List[T] => List[T] =
        selection andThen padPopulation andThen reproduce0

      @annotation.tailrec
      def search0(gen : Int, population : List[T], best: T): T = {
        if(gen == 0) best
        else {
          val next = evolutionOperator(population).sortBy { Memo(fitness) }.take(popSize)
          search0(gen - 1, next, List(next.head, best).minBy { fitness })
        }
      }

      search0(maxGens, population, population minBy fitness)
    }
}
