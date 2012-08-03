package whitewerx.com.trapos.translators

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import whitewerx.com.trapos.model._
import whitewerx.com.trapos.util.CurrencyProvider._
import whitewerx.com.trapos.util.CurrencyPairProvider._
import whitewerx.com.trapos.model.Amount
import whitewerx.com.trapos.model.Rate

/**
 * @author ludo
 */

class TradeTranslatorTest extends FunSpec with ShouldMatchers {

  describe("A trade translator") {

    it("should convert valid trade T|B|5.1t|R|EURUSD|1.3124 into 'Buy 5.1 thousand EUR at @ 1.3124 EURUSD.'") {
      val delimitedTrade = "T|B|5.1t|R|EURUSD|1.3124"
      val fivePointOneThousand: Amount = Amount(5.1 * 1000, EUR)
      val atEURUSDRate: Rate = Rate(1.3124, EUR_USD)
      val expected = Some(Purchase(fivePointOneThousand, atEURUSDRate))

      val result = TradeTranslator.unapply(delimitedTrade)
      result should equal (expected)
    }

    it("should convert a trade message with no multiplier T|S|5.1|R|EURUSD|1.3124 into 'Sell 5.1 EUR at @ 1.3124 EURUSD.'") {
      val delimitedTrade = "T|S|5.1|R|EURUSD|1.3124"
      val fivePointOne: Amount = Amount(5.1, EUR)
      val atEURUSDRate: Rate = Rate(1.3124, EUR_USD)
      val expected = Some(Sell(fivePointOne, atEURUSDRate))

      val result = TradeTranslator.unapply(delimitedTrade)
      result should equal (expected)
    }

    it("should convert a trade message with millions multiplier T|S|2m|R|USDCAD|1.0012 into 'Sell 2 million USD at @ 1.0012 USDCAD.'") {
      val delimitedTrade = "T|S|2m|R|USDCAD|1.0012"
      val fivePointOne: Amount = Amount(2 * 1000000, USD)
      val atUSDCADRate: Rate = Rate(1.0012, USD_CAD)
      val expected = Some(Sell(fivePointOne, atUSDCADRate))

      val result = TradeTranslator.unapply(delimitedTrade)
      result should equal (expected)
    }

    it("should not convert an invalid trade") {
      val invalidTrade = "T||5.1|R|EURUSD|1.3124"

      val result = TradeTranslator.unapply(invalidTrade)

      result should equal (None)
    }

    it("should not convert other messages") {
      val otherMessage = "C|S|5.1|R|EURUSD|1.3124"

      val result = TradeTranslator.unapply(otherMessage)

      result should equal (None)
    }

  }

}
