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

    for (
      TradeRegex("B", amount, multiplier, unparsedRate) <- delimitedTrade
      Some(rate) <- RateTranslator.unapply(unparsedRate)
    ) yield Purchase(Amount(amount.toDouble, Currency("EUR")), rate)

    /*delimitedTrade match {
      case TradeRegex("B", amount, multiplier, unparsedRate) =>
        println ("Match B " + amount + " " + multiplier + " " + unparsedRate)
        RateTranslator.unapply(unparsedRate) match {
          case Some(rate) =>
            println ("Match rate")
            Some(Purchase(Amount(amount.toDouble, Currency("EUR")), rate))
          case _ => None
        }

      case TradeRegex("S", amount, multiplier, unparsedRate) =>
        println ("Match S " + amount + " " + multiplier + " " + unparsedRate)
        RateTranslator.unapply(unparsedRate) match {
          case Some(rate) =>
            println ("Match rate")
            Some(Sell(Amount(amount.toDouble, Currency("EUR")), rate))
          case _ => None
        }

      case _ =>
        println ("No match")
        None
    }*/
  }

}