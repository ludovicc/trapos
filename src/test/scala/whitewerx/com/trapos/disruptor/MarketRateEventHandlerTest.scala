package whitewerx.com.trapos.disruptor

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import whitewerx.com.trapos.translators.RateTranslator
import whitewerx.com.trapos.model.Rate

/**
 * @author ludo
 */

class MarketRateEventHandlerTest extends FunSpec with ShouldMatchers with MockitoSugar {

  describe("A market rate event handler") {

    it("should consume a rate event") {
      val delimitedRate = "R|GBPUSD|1.6324"
      val rate = mock[Rate]
      val marketEvent: MarketEvent = mock[MarketEvent]
      val rateTranslator = mock[RateTranslator]
      val handler = new MarketRateEventHandler(rateTranslator)

      when(marketEvent.message).thenReturn(delimitedRate)
      when(rateTranslator.unapply(delimitedRate)).thenReturn(Some(rate))

      handler.onEvent(marketEvent, 1, false)

      verify(marketEvent).accept(rate)
    }

    it("should not consume a trade event") {
      val delimitedTrade = "T|S|2.3m|R|GBPUSD|1.6324"

      val marketEvent: MarketEvent = mock[MarketEvent]
      val rateTranslator = mock[RateTranslator]
      val handler = new MarketRateEventHandler(rateTranslator)

      when(marketEvent.message).thenReturn(delimitedTrade)
      when(rateTranslator.unapply(delimitedTrade)).thenReturn(None)

      handler.onEvent(marketEvent, 1, false)

      verify(marketEvent).message
      verifyNoMoreInteractions(marketEvent)
    }
  }
}
