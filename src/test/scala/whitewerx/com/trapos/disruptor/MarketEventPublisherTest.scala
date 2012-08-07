package whitewerx.com.trapos.disruptor

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._

/**
 * @author ludo
 */

trait RingBuffer {
   def next: Long
   def get(sequence: Long): MarketEvent
   def publish(sequence: Long): Unit
}

class MarketEventPublisherTest extends FunSpec with ShouldMatchers with MockitoSugar {

  describe("A market event publisher") {

    it("should publish an event") {
      val delimitedMessage = "T|B|5.1t|R|EURUSD|1.3124"
      val emptyEvent: MarketEvent = mock[MarketEvent]
      val ringBuffer = mock[RingBuffer]
      val publisher = new MarketEventPublisher(ringBuffer)
      val SEQUENCE: Long = 1

      when(ringBuffer.next).thenReturn(SEQUENCE)
      when(ringBuffer.get(SEQUENCE)).thenReturn(emptyEvent)

      publisher.accept(delimitedMessage)

      verify(emptyEvent).message_= (delimitedMessage)
      verify(ringBuffer).publish(SEQUENCE)
    }

  }
}
