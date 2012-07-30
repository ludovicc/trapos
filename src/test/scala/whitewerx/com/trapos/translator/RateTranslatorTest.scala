package whitewerx.com.trapos.translator

import org.scalatest.{GivenWhenThen, FunSpec}
import org.scalatest.matchers.ShouldMatchers
import whitewerx.com.trapos.model.translators.RateTranslator
import whitewerx.com.trapos.model.Rate
import whitewerx.com.trapos.util.CurrencyPairProvider._

/**
 * @author ludo
 */

class RateTranslatorTest extends FunSpec with ShouldMatchers {

  describe ("A rate traslator") {

    it ("should convert valid rate 'R|EURUSD|1.3124'") {
      val delimitedRate = "R|EURUSD|1.3124"
      val expected = Some(Rate(1.3124, EUR_USD))

      val result = RateTranslator.unapply(delimitedRate)
      result should equal (expected)
   }

    it ("should not convert an invalid rate") {
      val delimitedRate = "EURUSD|1.3124"

      val result = RateTranslator.unapply(delimitedRate)

      result should equal (None)
   }

  }

}
