package lectures.part3concurrency

import java.util.concurrent.Executors

object $21_Intro extends App {

  /*
  interface Runnable {
   public void run()
  }
  */

  // JVM Thread - is instance of the class

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  aThread.start() // gives the signal to the JVM to start a JVM thread
  //create a JVM thread => OS thread
  aThread.run() // doesn't do anything in parallel

  aThread.join() // blocks until aThread finishes running

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))

  threadHello.start()
  threadGoodbye.start()

  //different runs produce different results

  //executors - we want to use it because starting/stopping threads is efficiently expensive

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("A done after 1 second")
    Thread.sleep(1000)
    println("A done after 2 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("B almost done after 1 second")
    Thread.sleep(1000)
    println("B done after 2 second")
  })

  pool.shutdown()
  //pool.execute(() => println("should not appear")) // this thrown an exception in the calling thread because its shutdowned

  //pool.shutdownNow()
  println(pool.isShutdown) // true
}
