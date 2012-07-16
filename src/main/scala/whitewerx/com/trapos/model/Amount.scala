/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/15/12
 * Time: 1:34 AM
 * To change this template use File | Settings | File Templates.
 */

case class Amount(val amount: Double, val currency: Currency) {

  def currencyMatches(other: Currency): Boolean = currency == other

  override def toString() = "Amount [" + raw + " " + currency + "]"

}