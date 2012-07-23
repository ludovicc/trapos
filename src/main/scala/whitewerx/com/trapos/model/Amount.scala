package whitewerx.com.trapos.model

/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/15/12
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */

case class Amount(val amount: Double, val currency: Currency) {

  def currencyMatches(other: Currency): Boolean = currency == other

  override def toString() = "Amount [" + amount + " " + currency + "]"

  def +(other: Amount): Amount = {
    // TODO: better check
    if (!(this.currency == other.currency)) throw new IllegalArgumentException("Currency mismatch.  Trying to add " + this + " to " + other)

    Amount(this.amount + other.amount, this.currency)
  }

  def -(other: Amount): Amount = {
    // TODO: better check
    if (!(this.currency == other.currency)) throw new IllegalArgumentException("Currency mismatch.  Trying to add " + this + " to " + other)

    Amount(this.amount - other.amount, this.currency)
  }

  /**
   * Converts an amount to a quote currency at the specified rate.
   *
   * <pre>
   * this == 5m EUR
   * becomes == 6m USD (with a rate of 1.2)
   * </pre>
   *
   * @param quoteCurrency of the underlying rate.
   * @param atRate the conversion rate in terms of the base/quote.
   * @return
   */
  def convertToQuote(quoteCurrency: Currency, atRate: Double): Amount = {
    Amount(amount * atRate, quoteCurrency)
  }
}