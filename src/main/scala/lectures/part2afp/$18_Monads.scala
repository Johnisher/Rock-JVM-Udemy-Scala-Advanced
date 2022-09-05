package lectures.part2afp

object $18_Monads extends App {

  trait Attempt[+A] {

    //our own Try monad
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] = //identity, wrapping
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =  // bind
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing]{
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }
  val a = Attempt(List(1,2,3)) // 2 is wrapped by monad // apply returned the value with Attempt[Int] type
  val f = (v: Int) => Attempt("mama")
  val c = Attempt(2).flatMap(f)
  println()
  println("c "+ c)
  println("f " + f(5))
  println(c==f)

  val fn = (i: Int) => List(i-1, i, i+1)
  println(fn(10))
  val list1 = List(4,5,6)
  println(list1.flatMap(fn))



  /*
  left-identity

  unit(x).flatMap(f) = f(x)
  */
}
