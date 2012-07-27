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

    it ("should have a two million position average rate") {

      val positionEURUSD = Position.createFlatPositionFor(EUR_USD)
      val expectedPosition = create2MillionEURUSDPosition

      val result = positionEURUSD.add(buy_5m_EUR_USD).add(sell_3m_EUR_USD)

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

  /**
     * This position is the expected position for the following trades:
     *
     * <pre>
     * buy5mEURUSD
     * sell3mEURUSD
     * </pre>
     *
     * @return
     */
    private def create2MillionEURUSDPosition: Position = {
      val ccy1 = Amount((5 * ONE_MILLION) - (3 * ONE_MILLION), EUR)
      val ccy2 = Amount((-5 * ONE_MILLION * 1.3150) + (3.0 * ONE_MILLION * 1.3160), USD)
      val ccy1USDEquivalent = Amount((5 * ONE_MILLION * 1.3150) + (-3.0 * ONE_MILLION * 1.3160), USD)
      val ccy2USDEquivalent = ccy2

      Position(ccy1, ccy2, ccy1USDEquivalent, ccy2USDEquivalent, EUR_USD, USD)
    }
  }
