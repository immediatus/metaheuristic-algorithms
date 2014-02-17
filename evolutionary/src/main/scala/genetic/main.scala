package ua.org.scala
package genetic

object app {
  import scala.util.Random

  type Individual = Array[Boolean]

  def itod(in : Individual): Double =
    (0L /: in) { (acc, b) => (acc << 1) + (if(b) 1 else 0) } / 1000.0

  def random(bits : Int): Individual =
    Array.fill[Boolean](bits) { Random.nextBoolean }

  def oneMin(value: Double): Individual => Double =
    in => { // for sqrt function
      val r = itod(in)
      Math.abs(value - (r * r))
    }

  def mutation(rate : => Double): Individual => Individual =
    in => in.map { ch => if(Random.nextDouble < rate) !ch else ch }

  def crossover(rate : => Double): (Individual, Individual) => Individual =
    (parentA, parentB) => {
      if(Random.nextDouble >= rate) parentA
      else {
        val point = 1 + Random.nextInt(parentB.length - 1)
        parentA.splitAt(point)._1 ++ parentB.splitAt(point)._2
      }
   }

  def main(args : Array[String]) {
    import core._

    //algorithm configuration
    val crossoverF  = crossover(0.98)
    val mutationF   = mutation(0.015)
    val fitnessF    = oneMin(20)
    val selectionF  = selection.tournament[Individual]
    val searchF     = genetic.search[Individual](crossoverF, mutationF, fitnessF, selectionF, 1000, 100)
    val population  = List.fill(100) { random(16) }

    //search for best result
    val best = searchF(population)
    println(itod(best))
  }
}

