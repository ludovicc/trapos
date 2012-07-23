package whitewerx.com.trapos.model

/**
 * Represents a currency pair in the market convention.
 *
 * <pre>
 * EUR/USD
 * GPB/USD
 * USD/CAD
 * USD/JPY
 * </pre>
 *
 * http://en.wikipedia.org/wiki/Currency_pair
 *
 * @author ewhite
 */
case class CurrencyPair(val base: Currency, quote: Currency)
