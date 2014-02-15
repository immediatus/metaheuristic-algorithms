package ua.org.scala

package object core {
  implicit def timesExt(repeat : Int) = new {
    def times[T](f: Int => T): List[T] = {
      def times0(i : Int) : List[T] = if(i < repeat) f(i) :: times0(i + 1) else Nil
      times0(0)
    }
  }

  import scala.util.{Try, Success, Failure}
  implicit def sequenceExt[T](xs : List[Try[T]]) = new {
    def sequece: Try[List[T]] = {
      val (ss: List[Success[T]]@unchecked, fs: List[Failure[T]]@unchecked) =
      xs.partition(_.isSuccess)

      if (fs.isEmpty) Success(ss map (_.get))
      else Failure[List[T]](fs(0).exception)
    }
  }

  case class Memo[-T, +R](f : T => R) extends Function1[T, R] {
    private[this] val _memo = collection.mutable.Map.empty[T, R]
    def apply(x : T): R = _memo.getOrElseUpdate(x, f(x))
  }
}
