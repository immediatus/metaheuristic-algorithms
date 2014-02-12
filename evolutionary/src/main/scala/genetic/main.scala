package ua.org.scala
package genetic

object geneticModule {

  def atod(in : String): Double = {
    (0L /: in.toCharArray) { (acc, b) => (acc << 1) + (if(b == '0') 0 else 1) } / 1000.0
  }

  def oneMax(in : String): Double  = { // sqrt function
    val value = 16.0
    val r = atod(in)
    val delta = value - (r * r)
    if(delta == 0) Double.MaxValue else Math.abs(1 / delta)
  }

  def random(bits : Int): String = {
    import scala.util.Random._
    ("" /: (0 to bits)) { (acc, _) => acc + (if(nextBoolean) "1" else "0") }
  }

  def tournament(population : List[String]): List[String] = {
    import scala.util.Random._
    val length = population.length - 1
    population.map { _ => population(nextInt(length)) }
  }

  def mutation(in : String, rate : Double): String = {
    import scala.util.Random._, Math._
    in.map {
      case '0'  => if(abs(nextGaussian) < rate) '1' else '0'
      case '1'  => if(abs(nextGaussian) < rate) '0' else '1'
    }.mkString
  }

  def crossover(parentA : String, parentB : String, rate : Double): String = {
    import scala.util.Random._, Math._
    if(abs(nextGaussian) >= rate) parentA
    else {
      val point = 1 + nextInt(parentB.length - 2)
      val (a, _) = parentA.splitAt(point)
      val (_, b) = parentB.splitAt(point)
      (a ++ b).mkString
    }
  }

  def reproduce(selected : List[String], crs : Double, mtt: Double): List[String] = {
    def reproduce0(parent : List[String]): List[String] = {
      parent match {
        case a :: b :: tail => 
          mutation(crossover(a, b, crs), mtt) ::
          mutation(crossover(b, a, crs), mtt) ::
          reproduce0(tail)
        case _ => Nil
      }
    }
    reproduce0(if(selected.length % 2 == 0) selected else selected :+ selected.head)
  }

  def search(maxGens : Int, numBits : Int, popSize : Int, crs: Double, mtt : Double): String = {
    val pop = List.fill(popSize) { random(numBits) }
    val bst = pop.maxBy { oneMax(_) }

    @annotation.tailrec
    def search0(gen : Int, population : List[String], best: String): String = {
      if(gen == 0) best
      else {
        val next = reproduce(tournament(population), crs, mtt).sortBy { oneMax(_) }.take(popSize)
        val max = (next.head :: best :: Nil).maxBy { oneMax(_) }
        search0(gen - 1, next, max)
      }
    }
    search0(maxGens, pop, bst)
  }

  def main(args : Array[String]) {
    val best = search(100, 16, 100, 0.98, 0.015)
    println(atod(best))
  }
}

