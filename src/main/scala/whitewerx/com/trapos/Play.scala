package whitewerx.com.trapos

import java.util.concurrent.{Executors, ExecutorService}


/**
 * @author ludo
 */
object Play extends App {

    case class ThreadBound(thread: Thread) {
      trait ThreadConnector {
        require (Thread.currentThread() == thread)
      }

    }

  def thread1 = new Thread("thread-1")
  def thread2 = new Thread("thread-2")

  def inThread1 = ThreadBound(thread1)
  def inThread2 = ThreadBound(thread2)


}
