package whitewerx.com.trapos.translators

import whitewerx.com.trapos.model.{Rate, Currency, CurrencyPair}

/**
 * @author ludo
 */

trait RateTranslator {
  def unapply(delimitedRate: String) : Option[Rate]
}

object RateTranslator extends RateTranslator {
  /**Match RATE|(CCY1)(CCY2)|(rate as a double) */
  private val RateRegex = """^R\|([A-Z]{3})([A-Z]{3})\|(\d+(?:\.\d+)?)""".r

  def unapply(delimitedRate: String) : Option[Rate] = {

    delimitedRate match {
      case RateRegex(ccy1, ccy2, rate) => Some(Rate(rate.toDouble, CurrencyPair(Currency(ccy1), Currency(ccy2))))
      case _ => None
    }
  }

}