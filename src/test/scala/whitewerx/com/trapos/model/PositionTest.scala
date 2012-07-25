/**
 * @author ludo
 */

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers
import whitewerx.com.trapos.model.{Amount, Position, Currency}
import whitewerx.com.trapos.util.CurrencyProvider._
import whitewerx.com.trapos.util.CurrencyPairProvider.EUR_USD
import whitewerx.com.trapos.util.TradeProvider._

class PositionTest extends FunSpec with ShouldMatchers {

  describe ("A position") {

    it ("should add a trade to a flat position") {

      val positionEURUSD = Position.createFlatPositionFor(EUR_USD)
      val expectedPosition = create5point1MillionEURUSDPosition

      val result = positionEURUSD.add(buy_EUR_USD)

      result should equal (expectedPosition)
    }

  }

   /**
     * This position is the expected position for adding @buyEURUSD to a flat
     * position.
     */
    private def create5point1MillionEURUSDPosition: Position = {
      val ccy1 = Amount(5100, EUR)
      val ccy2 = Amount(-1 * 5100 * 1.3124, USD)
      val ccy1USDEquivalent = Amount(5100 * 1.3124, USD)
      val ccy2USDEquivalent = ccy2

      Position(ccy1, ccy2, ccy1USDEquivalent, ccy2USDEquivalent, EUR_USD, USD)
    }
  }
