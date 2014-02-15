package ua.org.scala
package core


trait Navigable[T] {
  def length: T => Int
  def iter:   T => Stream[T]
  def lens:   T => Stream[T => T]
  def prune:  T => Int => T
}

object Navigable {
  implicit def toNavigable[T : Navigable](obj : T) = new {
    def length: Int = implicitly[Navigable[T]].length(obj)
    def iter: Stream[T] = implicitly[Navigable[T]].iter(obj)
    def lens: Stream[T => T] = implicitly[Navigable[T]].lens(obj)
    def prune: Int => T = implicitly[Navigable[T]].prune(obj)
  }
}
