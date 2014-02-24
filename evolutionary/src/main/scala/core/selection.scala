package ua.org.scala
package core

object selection {

  def tournament[T]: List[T] => List[T] =
    population => {
      import util.Random._
      population.map { _ => population(nextInt(population.length)) }
    }

  def binaryTournament[T : Ordering]: List[T] => List[T] =
    population => {
      import util.Random._
      val compare = implicitly[Ordering[T]].compare _
      val length = population.length
      population.map { a =>
        val b = population(nextInt(length))
        if(compare(a, b) < 0) a else b
      }
    }
}
