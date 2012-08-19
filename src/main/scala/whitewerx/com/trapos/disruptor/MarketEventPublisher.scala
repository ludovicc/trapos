package whitewerx.com.trapos.disruptor

/**
 * @author ludo
 */

object MarketEventPublisher {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

  // Duck-typing for RingBuffer[MarketEvent], to allow mocking
  type RingBuffer = {
     def next: Long
     def get(sequence: Long): MarketEvent
     def publish(sequence: Long): Unit
  }

}

import MarketEventPublisher.RingBuffer
import whitewerx.com.trapos.gateway.TextMessageSubscriber

class MarketEventPublisher(private val ringBuffer: RingBuffer) extends TextMessageSubscriber {

  import MarketEventPublisher.logger
  import MarketEventPublisher.formatter._

  def accept(delimitedMessage: String) {
    val sequence: Long = ringBuffer.next
    val event = ringBuffer.get(sequence)

    event.message = delimitedMessage

    logger.info { _ ++= "publishEvent: seq:" ++= sequence.toString ++= " event:" ++= event.toString }

    ringBuffer.publish(sequence)
  }
}
