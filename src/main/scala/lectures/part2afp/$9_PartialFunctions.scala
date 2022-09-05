package lectures.part2afp

object $9_PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int    // this means aNicerFussyFunction is partial function because accept only the subset of ints

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // partial function value
  println(aPartialFunction(2))
  //println(aPartialFunction(23333))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67)) //  true/false

  //lift
  val lifted = aPartialFunction.lift // Int => Option[Int]
  println(lifted(2))
  println(lifted(332))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }
  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal function

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }
  //HOFs accept partial function as well

  val aMappedList = List (1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
  Note: PF can only have ONE parameter type
  */

  /**
   * Exercise
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }
    override def isDefinedAt(x: Int): Boolean =
      x == 1 || x == 2 || x == 5
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "No returned human"
    case "call mom" => "Unable to find"
  }
  scala.io.Source.stdin.getLines().foreach(line => println("chatbot says: " + chatbot(line)))
  //scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}
