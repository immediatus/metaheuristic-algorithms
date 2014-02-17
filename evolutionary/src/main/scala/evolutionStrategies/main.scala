package ua.org.scala
package evolutionStrategies

object app {
  import model._
  import core._

  type Individual = List[City]

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

    def orderCrossover[T](
      rate : Double
    ): (List[T], List[T]) => List[T] =
      (parentA, parentB) => {
        import util.Random._
        if(nextDouble >= rate) parentA
        else {
          val List(point1, point2) = List(nextInt(parentA.length), nextInt(parentA.length)).sorted
          Nil
        }
      }

  def main(args : Array[String]) {
    println(orderCrossover(0.5)(List(1,2,3,4,5), List(5,4,3,2,1)))

  }
}

