package lectures.part3concurrency

object $23_ThreadCommunication extends App {
  /*
   the producer-consumer problem

  producer -> [ x ] -> consumer

  */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int): Unit = value = newValue
    def get: Int = {
      val result = value
      value = 0
      result
    }
  }
  /*def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting..")
      while (container.isEmpty) {
        println("[consumer] actively waiting..")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(10)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value )
      container.set(value)
    })
    consumer.start()
    producer.start()
  }
  naiveProdCons()
*/
  //wait and notify

  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer  = new Thread(() => {
      println("[consumer] waiting..")
      container.synchronized {
        container.wait() // the producer thread will release the lock on the container (synchro) and it will suspend until producer will signal the container to continue
      }

      //container must have some value
      println("[consumer] I have consumed" + container.get)
    })
    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(1000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify() // object container notify another thread (any) which is under wait()
      }
    })

    consumer.start()
    producer.start()
  }
  smartProdCons()

}

//note
