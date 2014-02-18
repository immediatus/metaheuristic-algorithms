package ua.org.scala
package core

object evolutionStrategy {

  def search[T](
    iMutation:    T => T,
    sMutation:    T => T,
    fitness:      T => Double,
    maxGens:      Int,
    μ:            Int,
    λ:            Int
  ): List[T] => T =
    population => {

      @annotation.tailrec
      def search0(gen : Int, population : List[T], best: T): T = {
        import util.Random._
        if(gen == 0) best
        else {
          val current = population.sortBy { Memo(fitness) }
          val offspring = current.take(λ).map { iMutation andThen sMutation }
          val next = current.take(μ) ++ offspring
          search0(gen - 1, next, List(next.head, best).minBy { fitness })
        }
      }

      search0(maxGens, population, population minBy fitness)
    }
}
