package whitewerx.com.trapos.model

/**
 * @author ludo
 */

import org.scalatest._

import matchers.ShouldMatchers
import mock.MockitoSugar
import whitewerx.com.trapos.util.CurrencyProvider._
import org.mockito.Mockito._

class TradeTest extends FunSpec with ShouldMatchers with MockitoSugar {

  describe("A trade") {

    it("should calculate the quote amount") {

      val quoteAmount = Amount(2624800, USD)
      val tradeAmount = Amount(1000000, EUR)
      val rateEUR_USD = mock[Rate]
      when (rateEUR_USD.convert(tradeAmount)) thenReturn quoteAmount

      val trade = Purchase(tradeAmount, rateEUR_USD)

      val result = trade.quoteAmount

      verify(rateEUR_USD).convert(tradeAmount)
      result should equal (quoteAmount)
    }

  }

}
