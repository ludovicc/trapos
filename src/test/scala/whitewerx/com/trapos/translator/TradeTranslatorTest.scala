package whitewerx.com.trapos.translator

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers
import whitewerx.com.trapos.model.translators.RateTranslator
import whitewerx.com.trapos.model._
import whitewerx.com.trapos.util.CurrencyProvider._
import whitewerx.com.trapos.util.CurrencyPairProvider._
import whitewerx.com.trapos.model.Amount
import whitewerx.com.trapos.model.Rate
import scala.Some
import whitewerx.com.trapos.translators.TradeTranslator

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

    it("should not convert an invalid rate") {
/*      val delimitedRate = "EURUSD|1.3124"

      RateTranslator.unapply(delimitedRate) match {
        case Some(result: Rate) =>
          fail("a rate was not expected")
        case _ =>
      }*/
    }

  }

}
