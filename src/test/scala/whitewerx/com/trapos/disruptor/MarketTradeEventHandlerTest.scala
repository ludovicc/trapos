package whitewerx.com.trapos.disruptor

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import whitewerx.com.trapos.translators.TradeTranslator
import whitewerx.com.trapos.model.Trade

/**
 * @author ludo
 */

class MarketTradeEventHandlerTest extends FunSpec with ShouldMatchers with MockitoSugar {

  describe ("A market trade event handler") {

    it ("should consume a trade event") {
      val delimitedTrade = "T|S|2.3m|R|GBPUSD|1.6324"
      val trade = mock[Trade]
      val marketEvent: MarketEvent = mock[MarketEvent]
      val tradeTranslator = mock[TradeTranslator]
      val handler = new MarketTradeEventHandler(tradeTranslator)

      when(marketEvent.message).thenReturn(delimitedTrade)
      when(tradeTranslator.unapply(delimitedTrade)).thenReturn(Some(trade))

      handler.onEvent(marketEvent, 1, false)

      verify(marketEvent).accept(trade)
    }

    it ("should not consume a rate event") {
      val delimitedRate = "R|GBPUSD|1.6324"

      val marketEvent: MarketEvent = mock[MarketEvent]
      val tradeTranslator = mock[TradeTranslator]
      val handler = new MarketTradeEventHandler(tradeTranslator)

      when(marketEvent.message).thenReturn(delimitedRate)
      when(tradeTranslator.unapply(delimitedRate)).thenReturn(None)

      handler.onEvent(marketEvent, 1, false)

      verify(marketEvent).message
      verifyNoMoreInteractions(marketEvent)
    }
  }
}
