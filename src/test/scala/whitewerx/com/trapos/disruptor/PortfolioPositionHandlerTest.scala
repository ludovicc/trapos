package whitewerx.com.trapos.disruptor

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import whitewerx.com.trapos.model.{Trade, Position, PortfolioPosition}
import org.mockito.Mockito._

/**
 * @author ludo
 */

class PortfolioPositionHandlerTest extends FunSpec with ShouldMatchers with MockitoSugar {

  describe ("A portfolio position handler") {

    it ("should update the position for a trade event") {

      def portfolioPosition = mock[PortfolioPosition]
      def tradeEvent = mock[MarketEvent]
      def trade = mock[Trade]
      def updatedPosition = mock[Position]
      def handler = new PortfolioPositionHandler(portfolioPosition)

      when(tradeEvent.trade).thenReturn (Some(trade))
      when(portfolioPosition.add(trade)).thenReturn(updatedPosition)

      handler.onEvent(tradeEvent, 1, true)

    }
  }

}
