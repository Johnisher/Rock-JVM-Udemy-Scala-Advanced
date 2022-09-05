package lectures.part1as

object $6_AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description = numbers match {
    case head :: Nil => println(s"the only element is $head")
    case _ =>
  }
  description

  /*
  - constants
  - wildcards
  - case classes
  - tuples
  - some special magic like above
  */
  class Person( val age: Int, val name: String)
  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))
    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }
  val bob = new Person(22, "Bob")
  val gretting = bob match {
    case Person(n, age) => s"Hi my name is $n and Im $age yo."
  }
  // how it works:
  // 1. Runtime says: Ok this pattern called Person with (n, age)
  // 2. In such a case, looking for method unapply in object called Person which returns Tuple with 2 things (n, age) - have i found it ?
  // yes, is it empty ? its not
  // then pattern match

  println(gretting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)

  //Exercise

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }
  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 8
  val mathProperty = n match {
    case singleDigit() => "Single digit"
    case even() => " an even number"
    case _ => "no property"
  }
  println(mathProperty)

  //infix pattern

  case class Or[A, B](a: A, b: B) // Either
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string" // this infix works only if we have 2 arguments
  }
  println(humanDescription)

  // decomposing sequences

  val vararg = numbers match {
    case List(1,_*) => "starting with 1"
  }


  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]
  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if(list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }
  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting with 1,2"
    case _ => "smth else"
  }
  println(decomposed)

  // custom return types for unapply
  //REQUIRED FUNCTIONS: isEmpty: boolean, get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String]{
      def isEmpty = false
      def get = person.name
    }
  }
  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "AN aLINE"
  })

}
