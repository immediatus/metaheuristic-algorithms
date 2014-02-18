package ua.org.scala
package evolutionStrategies

object app {
  import model._
  import core._

  type Individual = (List[Double], List[City])

  def totalDistance: Individual => Double = {
    case (_, in) =>
      in.tail.foldLeft((0.0, in.head)) {
        case ((dist, a), b) =>
          val (latitude, longtitude) = (b.latitude - a.latitude, b.longtitude - a.longtitude)
          (dist + Math.sqrt(latitude * latitude + longtitude * longtitude), b)
      }._1
  }

  def orderMutation[T](
    mutationCount : Int,
  ): List[Double] => List[T] => List[T] =
    strategy => individual => {
      import util.Random._

      val elements = (individual zip strategy)
        .sortBy { case (_, st) => st }
        .map { case (e, _) => e }
        .take { nextInt(mutationCount + 1) }

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

  def countAdjustStrategy[T](mutationAmount : Int): Individual => Individual = {
    case (mutationCount, individual) =>
      (mutationCount, orderMutation(mutationCount.toInt, mutationAmount) apply individual)
  }

  def strategyMutation[T](value : Int): Individual => Individual = {
    case (stratgy, individual) =>
      import util.Random._
      val ta = 1 / (Math.sqrt(2 * Math.sqrt(value)))
      (stratgy * Math.exp(ta * nextDouble), individual)
  }

  def main(args : Array[String]) {
    import model._
    import core.Show, Show._
    import util.Random._

    val CITY_COUNT  = 20
    val cities    = europe.take(CITY_COUNT)

    val iMutationF  = countAdjustStrategy(CITY_COUNT)
    val sMutationF  = strategyMutation(CITY_COUNT)
    val fitnessF    = totalDistance
    val searchF     = evolutionStrategy.search[Individual](iMutationF, sMutationF, fitnessF, 1000, 30, 70)
    val population  = 100.times { _ => (nextInt(CITY_COUNT / 2).toDouble, shuffle(cities)) }

    val best @ (_, path) = searchF(population)
    println(path)
    println("distance: " + totalDistance(best))
  }
}

