package ua.org.scala
package evolutionStrategies

object app {
  import model._
  import core._

  type Individual = (List[City], List[Double])

  def totalDistance(in : Individual): Double =
    in.tail.foldLeft((0.0, in.head)) {
      case ((dist, a), b) =>
        val (latitude, longtitude) = (b.latitude - a.latitude, b.longtitude - a.longtitude)
        (dist + Math.sqrt(latitude * latitude + longtitude * longtitude), b)
    }._1

  def orderMutation[T](
    mutationCount : Int,
    mutationAmount : Int
  ): List[T] => List[T] =
    individual => {
      import util.Random._

      @annotation.tailrec
      def mutation0(i : Int, in : List[T]): List[T] =
        if(i == 0) in
        else {
          val fromIndex = nextInt(in.length)
          val toIndex = (fromIndex + nextInt(mutationAmount)) % in.length
          mutation0(i - 1, in.swap(fromIndex, toIndex))
        }
        mutation0(nextInt(mutationCount + 1), individual)
    }

  def mutationWithCountStrategy[T](mutationAmount : Int): (Int, List[T]) => (Int, List[T]) =
    (mutationCount, individual) => orderMutation(mutationCount, mutationAmount) apply individual

  def mutationWithAmountStrategy[T](mutationCount : Int): (Int, List[T]) => (Int, List[T]) =
    (mutationAmount, individual) => orderMutation(mutationCount, mutationAmount) apply individual

  def strategyMutation(value : Int): (Int, List[T]) => (Int, List[T]) = {
    case (stratgy, individual) =>
      import util.Random._
      (value * (0.7 * nextDouble + 0.7 * nextDouble), individual)
  }


  def main(args : Array[String]) {
    import util.Random._

    val CITY_COUNT  = 5
    val baseList    = cities.take(CITY_COUNT)

    val iMutationF  = mutation(0.015)
    val sMutationF  = mutation(0.015)
    val fitnessF    = totalDistance _
    val searchF     = evolutionStrategies.search[Individual](iMutationF, sMutationF, fitnessF, 1000, 70, 30)
    val population  = shuffle(baseList).map { c => (c, nextInt(CITY_COUNT / 2)) }

    val best = searchF(population)
    println("distance: " + totalDistance(best))
  }
}

