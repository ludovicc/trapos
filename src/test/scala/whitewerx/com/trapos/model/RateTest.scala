package whitewerx.com.trapos.model

/**
 * @author ludo
 */

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

import whitewerx.com.trapos.util.CurrencyProvider._
import whitewerx.com.trapos.util.CurrencyPairProvider._

class RateTest extends FunSuite with ShouldMatchers {

  test("convert to quote amount when the rate is foreign") {

    val baseAmount = Amount(2000000, EUR)
    val rateEURUSD = Rate(1.3124, EUR_USD)
    val expectedQuoteAmount = Amount(2624800, USD)

    val result = rateEURUSD.convert(baseAmount)

    result should equal (expectedQuoteAmount)
  }
}
