package lectures.part2afp

object $15_LazyEvaluation extends App{

  // val x: Int = throw new RuntimeException  // THIS WILL CRASH THE PROGRAM

  //lazy DELAYS the evaluation on values, lazy values evaluates once
  lazy val x: Int = throw new RuntimeException

  //it will crash only when i use the value:
  //println(x)

  lazy val y: Int = {
    println("hello")
    42
  }
  println(y)
  println(y)

  // examples of implications:

  // side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }
  def simpleCondition: Boolean = false
  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no") // runtime is smart and doesnt evaluate lazyCondition while simpleCondition is false.


  //in conjunction with call by name
  def byNameMethod(n: => Int): Int =
    //CALL BY NEED
    lazy val t = n
    t + t + t + 1  //// => arrow tells that parameter will be called by Name. value of the Name expression is computed every time is called inside the function
  def retrieveMagicalValue = {
    //side effect or a long computation
    println("waiting")
    //Thread.sleep(1000)
    42
  }
  println(byNameMethod(retrieveMagicalValue))

  // filtering with lazy vals

  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }
  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }
  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List (1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println
  gt20lazy.foreach(println)

  //for-comprehensions use withfilter with guards

  for {
    a <- List(1,2,3) if a%2 == 0 // if guards use lazy vals!
  } yield a + 1
  //it is the same:
  val cos = List(1,2,3).withFilter(_ % 2 == 0).map(_ + 1) // withFilter transforms to monadic and .map transforms back to INT
  println(cos)

  /*
  Exercise: Implement a lazily evaluated, singly linked STREAM of elements


  naturals = MyStream.from(1)(x=> x+1) = stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println) // will crash - infinite!
  naturals.map(_*2) // stream of all even numbers (potentially infinite)
  */

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] //prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B]

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A]
    def takeAsList(n: Int): List[A]

    object MyStream {
      def from[A](start: A)(generator: A => A): MyStream[A] = ???
    }

  }


}
