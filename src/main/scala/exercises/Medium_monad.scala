package exercises

object Medium_monad extends App{

  trait M[+A] {
    def flatMap[B](f: A => M[B]): M[B] // binding
  }
  object M {
    def apply[A](x: A): M[A] = ??? //identity (wrapping part)
  }

  val fn = (i: Int) => List(i-1, i, i+1)
  println(fn(10))
  val list1 = List(4,5,6)
  println(list1.flatMap(fn))

}
