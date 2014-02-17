package ua.org.scala
package core

object selection {

  def tournament[T]: List[T] => List[T] =
    population => {
      population.map {
        _ => population(util.Random.nextInt(population.length)) 
      }
    }
}
