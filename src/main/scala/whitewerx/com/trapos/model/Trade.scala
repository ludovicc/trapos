/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/17/12
 * Time: 12:39 AM
 * To change this template use File | Settings | File Templates.
 */

sealed abstract case class Trade(val amount: Amount, val dealRate: Rate) {

}

case class Purchase(override val amount: Amount, override val dealRate: Rate) extends Trade(amount, dealRate)
case class Sell(override val amount: Amount, override val dealRate: Rate) extends Trade(amount, dealRate)
