package whitewerx.com.trapos.disruptor

import org.scalatest.{BeforeAndAfter, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import whitewerx.com.trapos.model.{Trade, Position, PortfolioPosition}
import org.mockito.Mockito._
import whitewerx.com.trapos.model.event.{PositionChangeEvent, DomainEvents}

/**
 * @author ludo
 */

class PortfolioPositionHandlerTest extends FunSpec with ShouldMatchers with MockitoSugar with BeforeAndAfter {

  var count = 0

  before {
  }

  after {
     DomainEvents.removeSubscriptions()
  }

  describe ("A portfolio position handler") {

    it ("should update the position for a trade event") {

      val portfolioPosition = mock[PortfolioPosition]
      val marketEvent = mock[MarketEvent]
      val trade = mock[Trade]
      val updatedPosition = mock[Position]
      val handler = spy(new PortfolioPositionHandler(portfolioPosition))

      when(marketEvent.trade).thenReturn(Some(trade))
      when(portfolioPosition.add(trade)).thenReturn(updatedPosition)
      DomainEvents.subscribe(handler)

      handler.onEvent(marketEvent, 1, true)

      verify(handler).notify(DomainEvents, PositionChangeEvent(updatedPosition))

    }
  }

}
