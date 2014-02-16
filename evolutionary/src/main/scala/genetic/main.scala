package ua.org.scala
package genetic

object app {

  def atod(in : String): Double = {
    (0L /: in) { (acc, b) => (acc << 1) + (if(b == '0') 0 else 1) } / 1000.0
  }

  def random(bits : Int): String = {
    import scala.util.Random._
    ("" /: (0 to bits)) { (acc, _) => acc + (if(nextBoolean) "1" else "0") }
  }

  def oneMin(value: Double): String => Double =
    in => { // for sqrt function
      val r = atod(in)
      Math.abs(value - (r * r))
    }

  def mutation(rate : => Double): String => String =
    in => {
      import scala.util.Random._, Math._
      in.map {
        case '0'  => if(nextDouble < rate) '1' else '0'
        case '1'  => if(nextDouble < rate) '0' else '1'
      }.mkString
    }

  def crossover(rate : => Double): (String, String) => String =
    (parentA, parentB) => {
      import scala.util.Random._, Math._
      if(nextDouble >= rate) parentA
      else {
        val point = 1 + nextInt(parentB.length - 1)
        val (a, _) = parentA.splitAt(point)
        val (_, b) = parentB.splitAt(point)
        (a ++ b).mkString
      }
   }

  def main(args : Array[String]) {
    import core._

    //algorithm configuration
    val crossoverF  = crossover(0.98)
    val mutationF   = mutation(0.015)
    val fitnessF    = oneMin(20)
    val selectionF = genetic.tournament[String]
    val searchF     = genetic.search[String](crossoverF, mutationF, fitnessF, selectionF, 1000, 100)
    val population  = List.fill(100) { random(16) }

    //search for best result
    val best = searchF(population)
    println(atod(best))
  }
}

