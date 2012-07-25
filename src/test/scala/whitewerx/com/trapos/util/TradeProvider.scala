package whitewerx.com.trapos.util

import whitewerx.com.trapos.util.CurrencyPairProvider.EUR_USD
import whitewerx.com.trapos.model._
import whitewerx.com.trapos.model.Amount
import whitewerx.com.trapos.model.Rate
import whitewerx.com.trapos.model.Purchase

/**
 * Sample trades to test with.
 *
 * @author ewhite
 */
object TradeProvider {
  private def createSimpleEURUSDTrade: Trade = {
    val fivePointOneThousand: Amount = new Amount(5.1 * 1000, CurrencyProvider.EUR)
    val atEURUSDRate: Rate = new Rate(1.3124, EUR_USD)
    Purchase(fivePointOneThousand, atEURUSDRate)
  }

  private def create5mEURUSDTrade: Trade = {
    val amount: Amount = new Amount(5 * ONE_MILLION, CurrencyProvider.EUR)
    val atEURUSDRate: Rate = new Rate(1.3150, EUR_USD)
    Purchase(amount, atEURUSDRate)
  }

  private def create3mEURUSDTrade: Trade = {
    val amount: Amount = new Amount(3 * ONE_MILLION, CurrencyProvider.EUR)
    val atEURUSDRate: Rate = new Rate(1.3160, EUR_USD)
    Sell(amount, atEURUSDRate)
  }

  val buy_EUR_USD: Trade = createSimpleEURUSDTrade
  val buy_5m_EUR_USD: Trade = create5mEURUSDTrade
  val sell_3m_EUR_USD: Trade = create3mEURUSDTrade
  val ONE_MILLION: Int = 1000 * 1000
}
