/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/17/12
 * Time: 12:41 AM
 * To change this template use File | Settings | File Templates.
 */

/**
 * A Rate specified in terms of the quote currency.
 *
 * So a EURUSD rate of 1.3123 means that 1 EUR buys 1.3123 USD. Where EUR is the
 * base currency, and USD is the quote currency.
 *
 * http://en.wikipedia.org/wiki/Currency_pair
 *
 * @author ewhite
 */
case class Rate(rate: Double, quotedPair: CurrencyPair) {

  override def toString: String = "Rate [" + quotedPair + "@" + rate + "]"

  /**
   * @return the base currency == CCY1
   */
  def baseCurrency: Currency = {
    this.quotedPair.base
  }

  /**
   * @return the quote currency == CCY2
   */
  private def quoteCurrency: Currency = {
    this.quotedPair.quote
  }

  def currencyPair: CurrencyPair = this.quotedPair

  /**
   * Convert the CCY1 amount to a CCY2 amount using the rate.
   *
   * This assumes that everything is foreign in terms of USD e.g. EURUSD,
   * GBPUSD, etc.
   *
   * @param baseAmount to convert
   * @return
   */
  def convert(baseAmount: Amount): Amount = {
    // TODO: more Scala-ish test
    if (!baseAmount.currencyMatches(baseCurrency)) throw new IllegalArgumentException("The base amount currency does not match the rate base currency. Amount: " + baseAmount + " Rate Base Currency: " + baseCurrency)

    val atRate: Double = this.rate
    baseAmount.convertToQuote(quoteCurrency, atRate)
  }

}