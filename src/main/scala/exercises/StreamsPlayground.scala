package exercises

import scala.annotation.tailrec
import scala.runtime.Nothing$


abstract class MyStream[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] //prepend operator

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]

  def takeAsList(n: Int): List[A] = take(n).toList()
/*
  [1 2 3].toList([]) =
  [2 3].toList([1]) =
  [3].toList([2 1]) =
  [].toList ([3 2 1]) =
  [1 2 3]

*/
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
  if (isEmpty) acc.reverse
  else tail.toList(head :: acc)
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException

  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)

  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream

  def foreach(f: Nothing => Unit): Unit = ()

  def map[B](f: Nothing => B): MyStream[B] = this

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this

}

class Cons[+A](hd: A , tl: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false

  override val head: A = hd
  override lazy val tail: MyStream[A] = tl // call by need

  //val s = new Cons (1, EmptyStream) // EmptyStream is lazy evaluated
  //val prepended = 1 #:: s = new Cons (1, s) // s is lazy evaluated
  def #::[B >: A](element: B): MyStream[B] = new Cons(element, this) //prepend operator
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream) // tail ++ anotherStream is lazy evaluated here

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }

    // s = new Cons(1, ?)
    // mapped = s.map(_ + 1) = new COns (2, s.tail.map(_ + 1)
    // ... mapped.tail  // only here the tail will be evaluated (by need)
  def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f)) // preserves lazy evaluation

  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] =
    if(predicate(head)) new Cons(head, tail.filter(predicate))
    else tail.filter(predicate) // preserves lazy evaluation

  def take(n: Int): MyStream[A] =
    if(n <= 0) EmptyStream
    else if (n == 1) new Cons(head, EmptyStream)
    else new Cons(head, tail.take(n-1))

}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new Cons(start, MyStream.from(generator(start))(generator))
}
object StreamsPlayground extends App{
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println((naturals.tail.head))
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals //naturals.#::(0)
  println(startFrom0.head)

  startFrom0.take(10000).foreach(println)

  //map, flatmap
  println(startFrom0.map(_ * 2).take(100).toList())
  println(startFrom0.flatMap(x=> new Cons(x, new Cons(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())

  //EXERCISE
  // fibonacci
  // [first, [...
  // first, fibo(second, first + second)
  def aFibonacci(first: Int, second: Int): MyStream[Int] =
  new Cons(first, aFibonacci(second, first+second))

  println(aFibonacci(1,1).take(100).toList())

  /*
  [2 3 4 5  6 7 8 9 10 11 12 ...]
  [2 3 5 7 9 11 13 ...
  [2 eratosthenes applied to (numbers filtered by n%3 != 0
  [2 3 5
  */


  //eratosthenes sieve
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if(numbers.isEmpty) numbers
    else new Cons (numbers.head, eratosthenes(numbers.tail.filter(_ % numbers.head != 0)))

  println(eratosthenes(MyStream.from(2)(_+1)).take(100).toList())



}
