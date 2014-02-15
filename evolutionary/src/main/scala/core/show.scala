package ua.org.scala
package core


trait Show[T] { def show: T => String }

object Show {
  implicit def showExtension[T : Show](value : T) = new {
    def show: String = implicitly[Show[T]].show(value)
  }
}
