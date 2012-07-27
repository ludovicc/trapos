package whitewerx.com.trapos.util

import whitewerx.com.trapos.util.CurrencyPairProvider.EUR_USD
import whitewerx.com.trapos.util.CurrencyProvider.EUR
import whitewerx.com.trapos.model._

/**
 * Sample trades to test with.
 *
 * @author ewhite
 */
object TradeProvider {
  private def createSimpleEURUSDTrade: Trade = {
    val fivePointOneThousand = Amount(5.1 * 1000, EUR)
    val atEURUSDRate = Rate(1.3124, EUR_USD)
    Purchase(fivePointOneThousand, atEURUSDRate)
  }

  private def create5mEURUSDTrade: Trade = {
    val amount = Amount(5 * ONE_MILLION, EUR)
    val atEURUSDRate = Rate(1.3150, EUR_USD)
    Purchase(amount, atEURUSDRate)
  }

  private def create3mEURUSDTrade: Trade = {
    val amount = Amount(3 * ONE_MILLION, EUR)
    val atEURUSDRate = Rate(1.3160, EUR_USD)
    Sell(amount, atEURUSDRate)
  }

  lazy val buy_EUR_USD: Trade = createSimpleEURUSDTrade
  lazy val buy_5m_EUR_USD: Trade = create5mEURUSDTrade
  lazy val sell_3m_EUR_USD: Trade = create3mEURUSDTrade
  val ONE_MILLION: Int = 1000 * 1000
}
