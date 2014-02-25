package ua.org.scala
package evolutionStrategies

object app {
  import model._
  import core._

  type Individual = (List[Double], List[City])

  def distanceCalculation[T]: List[City] => List[Double] =
    Memo(individual =>
      (0.0 :: (individual.tail.foldLeft((List.empty[Double], individual.head)) {
        case ((acc, a), b) =>
          val (latitude, longtitude) = (b.latitude - a.latitude, b.longtitude - a.longtitude)
          (Math.sqrt(latitude * latitude + longtitude * longtitude) :: acc, b)
      }._1))
    )

  def orderMutation[T](
    mutationCount : Int,
    mutationAmount : Int,
    strategy : List[Double],
    individual : List[T]
  ): List[T] = {
    import util.Random._

    val elements = (individual zip strategy)
      .sortBy { case (_, st) => st }
      .map { case (e, _) => e }
      .take { nextInt(mutationCount + 1) }

    @annotation.tailrec
    def mutation0(e : List[T], in : List[T]): List[T] =
      e match {
        case x :: xs =>
          val fromIndex = in.indexOf(x)
          val toIndex   = (fromIndex + nextInt(mutationAmount)) % in.length
          mutation0(xs, in.swap(fromIndex, toIndex))
        case _      => in
      }

    mutation0(elements, individual)
  }

  def totalDistance: Individual => Double = {
    case (strategy, individual) => distanceCalculation(individual).sum
  }

  def individualMutation(mutationCount : Int, mutationAmount : Int): Individual => Individual = {
    case (strategy, individual) => (strategy, orderMutation(mutationCount, mutationAmount, strategy, individual))
  }

  def strategyMutation: Individual => Individual = {
    case (strategy, individual) =>
      import util.Random._, Math._
      val tau = pow(sqrt(2.0 * strategy.length), -1.0)
      val tau_p = pow(sqrt(2.0 * sqrt(strategy.length)), -1.0)
      (distanceCalculation(individual).map { x => x * exp(tau_p * nextDouble + tau * nextDouble) }, individual)
  }

  def main(args : Array[String]) {
    import model._
    import core.Show, Show._
    import util.Random._

    val CITY_COUNT  = 30
    val cities    = europe.take(CITY_COUNT)

    val iMutationF  = individualMutation(5, CITY_COUNT / 2)
    val sMutationF  = strategyMutation
    val fitnessF    = totalDistance
    val searchF     = evolutionStrategy.search[Individual](iMutationF, sMutationF, fitnessF, 1000, 30, 70)
    val population  = 200.times { _ =>
      val individual = shuffle(cities)
      (distanceCalculation(individual), individual)
    }

    val best @ (_, path) = searchF(population)

    println(path.show)
    println("distance: " + totalDistance(best))
  }
}

