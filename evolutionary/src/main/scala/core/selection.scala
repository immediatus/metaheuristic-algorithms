package ua.org.scala
package core

object selection {

  def tournament[T]: List[T] => List[T] =
    population => {
      import util.Random._
      population.map { _ => population(nextInt(population.length)) }
    }
}
