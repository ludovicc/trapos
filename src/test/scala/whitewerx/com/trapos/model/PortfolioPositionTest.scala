package whitewerx.com.trapos.model

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import whitewerx.com.trapos.util.CurrencyPairProvider._
import whitewerx.com.trapos.util.TradeProvider._

/**
 * @author ludo
 */

class PortfolioPositionTest  extends FunSpec with ShouldMatchers with MockitoSugar {

  describe ("A portfolio position") {

    it ("should add a trade to a flat position") {
      val flatPosition = mock[Position]
      val updatedPosition = mock[Position]
      val positionFactory = mock[PositionFactory]

      when(positionFactory.createFlatPositionFor(EUR_USD)) thenReturn flatPosition
      when(flatPosition.add(buy_EUR_USD)) thenReturn updatedPosition

      val portfolioPosition = new PortfolioPosition(positionFactory)

      val result = portfolioPosition.add(buy_EUR_USD)

      result should equal (updatedPosition)

      verify(positionFactory) createFlatPositionFor(EUR_USD)
      verify(flatPosition) add(buy_EUR_USD)
      verifyNoMoreInteractions(flatPosition, updatedPosition, positionFactory)
    }

    it ("should add a trade to an existing position") {
      val positionEURUSD = mock[Position]
      val updatedPosition = mock[Position]
      val updatedPosition2 = mock[Position]
      val positionFactory = mock[PositionFactory]

      when(positionFactory.createFlatPositionFor(EUR_USD)) thenReturn positionEURUSD
      when(positionEURUSD.add(buy_EUR_USD)) thenReturn updatedPosition
      when(updatedPosition.add(buy_EUR_USD)) thenReturn updatedPosition2

      val portfolioPosition = new PortfolioPosition(positionFactory)

      portfolioPosition.add(buy_EUR_USD)
      val result = portfolioPosition.add(buy_EUR_USD)

      result should equal (updatedPosition2)

      verify(positionFactory) createFlatPositionFor(EUR_USD)
      verify(positionEURUSD) add(buy_EUR_USD)
      verify(updatedPosition) add(buy_EUR_USD)
      verifyNoMoreInteractions(positionEURUSD, updatedPosition, updatedPosition2, positionFactory)
    }

  }

}
