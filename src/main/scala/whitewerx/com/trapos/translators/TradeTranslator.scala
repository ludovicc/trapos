package whitewerx.com.trapos.translators

import whitewerx.com.trapos.model._
import scala.Some
import whitewerx.com.trapos.model.Purchase
import whitewerx.com.trapos.model.Rate
import whitewerx.com.trapos.model.translators.RateTranslator

/**
 * @author ludo
 */

object TradeTranslator {
  /**Match RATE|(CCY1)(CCY2)|(rate as a double) */
  private val TradeRegex = """^T\|([B|S])\|(\d+(?:\.\d+)?)([t,m]?)\|(R\|.*)""".r

  def unapply(delimitedTrade: String): Option[Trade] = {

    delimitedTrade match {
      case TradeRegex(tradeType, amount, multiplier, unparsedRate) =>
        RateTranslator.unapply(unparsedRate) match {
          case Some(rate) =>
            tradeType match {
              case "B" =>
                Some(Purchase(Amount(amount.toDouble * valueOf(multiplier), rate.baseCurrency), rate))
              case "S" =>
                Some(Sell(Amount(amount.toDouble * valueOf(multiplier), rate.baseCurrency), rate))
            }
          case _ => None
        }

      case _ =>
        None
    }
  }

  private def valueOf(multiplier: String): Double = {
    multiplier match {
      case "t" => 1000
      case "m" => 1000000
      case _ => 1
    }
  }
}