package lectures.part2afp

object $14_CurriesPAF extends App{

  //curried functions
  val superAdder: Int => Int => Int =
    x => y => x + y
  val add3 = superAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(superAdder(3)(5)) // curried function

  //METHOD!:
  def curriedAdder(x: Int)(y: Int): Int = x + y //curried method

  val add4: Int => Int = curriedAdder(4)

  //lifting = ETA-EXPANSION

  //functions != methods (JVM limitation)

  def inc(x: Int) = x + 1
  List(1,2,3).map(inc) // inc = x=> inc(x)   // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _// Int => Int

  //EXERCISE

  val simpleAddFunction = (x: Int, y: Int) => x + y
  def simpleMethod(x: Int, y: Int): Int = x + y
  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  //val add7: Int => Int = y => 7 + y

  val add7 = (y: Int) => simpleAddFunction(7 , y)
  val add74 = simpleAddFunction.curried(7)
  val add79 = simpleAddFunction(7 , _:Int)  // alternative syntax for turn method into function values

  val add71 = (y: Int) => simpleMethod(7, y)
  val add78 = simpleMethod(7, _: Int)
  // y => simpleAddMethod(7, y)

  val add72 = (y: Int) => curriedAddMethod(7)(y)
  val add73 = curriedAddMethod(7)
  val add75 = curriedAddMethod(7) _ // PAF
  val add76 = curriedAddMethod(7)(_) // PAF = alternative syntax

  //underscores are powerful

  def concatenator (a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you ?") // x: String = concatenator(hello, x, howareyou)
  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String) // (x, y) => concatenator("Hello, ", x,y)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  //EXERCISES


  def curriedFormatter (c: String)(number: Double): String = c.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormat = curriedFormatter("%4.2f") _ // lift
  val seriousFormat = curriedFormatter("%8.6f") _
  val preciseFormat = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormat))
  println(numbers.map(seriousFormat))
  println(numbers.map(preciseFormat))

  //-----------
  def byName(n: => Int) = n + 1
  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42
  def parenMethod(): Int = 42
/*
calling byName and byFunction
  - int
  - method
  - parenMethod
  - lambda
  - PAF
*/
  byName(23) // ok
  byName(method) // ok
  byName(parenMethod())
  //byName(() => 42) // not ok
  byName((() => 42)()) // ok
  //byName(parenMethod _) // not ok

  //byFunction(42) // not ok
  //byFunction(method) // not ok!! does not do ETA-expansion!
  byFunction(parenMethod) // compiler does ETA-expansion!
  byFunction(() => 46) // ok
  byFunction(parenMethod _) // ok


  //-----
  def urlPages (a: String)(b: String): String = a + "/" + b

  val homeUrls = urlPages("Home.pl")
  val careersUrls = urlPages("Home/Career")

  val home = homeUrls("")
  val login = homeUrls("login")
  val products = homeUrls("products")
  val career = careersUrls("HR")

  println(home)
  println(login)
  println(products)
  println(career)

}
