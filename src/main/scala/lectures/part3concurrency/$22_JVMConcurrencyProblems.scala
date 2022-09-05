package lectures.part3concurrency

object $22_JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0  // mutable

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })
    thread1.start()
    thread2.start()

    Thread.sleep(100)
    println(x) // race condition
  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int) = {
    /*
    involves 3 steps:
    - read old value
    - compute result
    - write new value
    */

    bankAccount.amount -= price
  }
  def buySafe(bankAccount: BankAccount, thing: String, price: Int) = {
    bankAccount.synchronized { // sync do not allow multiple threads to run the critical section at the same time
      bankAccount.amount -= price // critical section
    }
  }
  /*
    Example race condition:
    thread1 (shoes)
      - reads amount 50000
      - compute result 50000 - 3000 = 47000
    thread2 (iPhone)
      - reads amount 50000
      - compute result 50000 - 4000 = 46000
    thread1 (shoes)
      - write amount 47000
    thread2 (iPhone)
      - write amount 46000
  */

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach{_ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
      val thread2 = new Thread(() => buySafe(account, "iPhone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"Aha  I've just broken the bank: ${account.amount}")
    }
  }

  /**
   Exercise
   1 - create "inception threads"
    thread1
        -> thread 2
              -> thread 3
                      ....
            each thread prints "hello from thread $i"
            Print all messages IN REVERSE ORDER
   2 - whats the max/min value of x
   3 - "sleep fallacy": whats the value of message?
   */
  //1

  def inceptionThread(max: Int, n: Int = 1): Thread = {
    new Thread(() => {
      if (n < max) {
        val newThread = inceptionThread(max, n + 1)
        newThread.start()
        newThread.join()
      }
      println(s"hello from thread $n")
    })
  }


  //2
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }
  //3
  def demoSleepFallacy(): Unit = {
    var message = " "
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)

    println(message)
  }



  def main(args: Array[String]): Unit = {
    //runInParallel()
    //demoBankingProblem()
    //inceptionThread(10).start()
    demoSleepFallacy()

  }
}

/*

1. Create val x and 2 threads. One thread will add 1 to the value, and the other will add 2 to the value. Print it.

*/