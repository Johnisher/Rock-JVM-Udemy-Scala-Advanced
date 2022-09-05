package lectures.part1as

import scala.util.Try

object $5_DarkSugars extends App {

  // #1: methods with single param

  def singleArgMethod(arg: Int): String = s"$arg little ducks.."
  val description = singleArgMethod {
    //code
    42
  }
  val aTryInstance = Try { // javas try {..}
    throw new RuntimeException
  }
  List(1,2,3).map { x=>
    x + 1
  }

  //#2 single abstract method

  trait Action {
    def act(x: Int): Int
  }
  val anInstance: Action =  new Action {
    override def act(x: Int): Int = x + 1
  }
  val aFunkyInstance: Action = (x: Int) => x + 1
  //example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello")
  })
  val aSweeterThread = new Thread(() => println("swetter"))

  abstract class AnAbstractType {
    def implemented: Int = 33
    def non(a: Int): Unit // the only unimplemented method ->
  }
  val anAbstractInstance: AnAbstractType = (a: Int) => println("scala")  // -> implementing the method.

  //syntax sugar #3: the :: and #:: methods are special
  val prepended = 1 :: 2 :: List(3,4)
  println(prepended)

  // 4. multi-word method naming
  class TeenGirl(name: String) {
    def `and then said` (gossip: String) = println(s"$name said $gossip") // method name with backthinks (also tilda)
  }
  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet"

  // 5. Infix types
  class Composite[A, B]
  val composite: Composite[Int, String] = ???
  val composite2: Int Composite String = ???

  class --> [A, B]
  val towards: Int --> String = ???

  // 6. Update() - is very special much like apply()

  val anArray = Array(1,2,3)
  anArray(2) = 7 // rewritten to anArray.update(2, 7)
  // used in mutable collections
  //remember apply() AND update() !

  // 7. setters for mutable container

  class Mutable {
    private var internalMember: Int = 0 // private for OO encapsulation
    def member = internalMember // getter
    def member_=(value: Int): Unit =
      internalMember = value // setter
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as aMutableContainer.member_(42)


}
