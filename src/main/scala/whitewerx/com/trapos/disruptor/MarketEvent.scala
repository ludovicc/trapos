package whitewerx.com.trapos.disruptor

import whitewerx.com.trapos.model.{Rate, Trade}
import com.lmax.disruptor.EventFactory

/**
 * @author ludo
 */

class MarketEvent extends Mutable {

  private var _delimitedMessage: String = _

  /**Will be non null if a trade event was received. */
  private var _trade: Option[Trade] = _
  /**Will be non null if a rate event was received. */
  private var _rate: Option[Rate] = _

  /**
   * Returns the delimited string message.
   */
  def message: String = return _delimitedMessage

  def message_=(delimitedMessage: String): Unit = {
    _delimitedMessage = delimitedMessage
    reset
  }

  private def reset {
    this._trade = None
    this._rate = None
  }

  /**
   * @return the trade populated on the event.
   */
  def trade: Option[Trade] = _trade

  def accept(trade: Trade) {
    this._trade = Some(trade)
  }

  def accept(rate: Rate) {
    this._rate = Some(rate)
  }

  @Override override def toString: String = {
    "MarketEvent [delimitedMessage=" + _delimitedMessage + ", trade=" + _trade + ", rate=" + _rate + "]"
  }

}

object MarketEvent {

  val FACTORY = new EventFactory[MarketEvent] {
    def newInstance: MarketEvent = new MarketEvent

  }
}