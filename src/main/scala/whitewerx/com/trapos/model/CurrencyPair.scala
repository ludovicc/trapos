/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/17/12
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */

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
