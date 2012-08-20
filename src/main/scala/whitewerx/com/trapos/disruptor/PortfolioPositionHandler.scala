package whitewerx.com.trapos.disruptor

import whitewerx.com.trapos.model.PortfolioPosition
import whitewerx.com.trapos.model.event._
import collection.mutable
import com.lmax.disruptor.EventHandler

/**
 * @author ludo
 */

object PortfolioPositionHandler {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

}

class PortfolioPositionHandler(
    /** Cached positions */
    private val portfolioPosition: PortfolioPosition)
      extends EventHandler[MarketEvent] with mutable.Subscriber[Event, DomainEventsPublisher] {

  import PortfolioPositionHandler.logger
  import PortfolioPositionHandler.formatter._

  private var currentSequence: Long = 0L

  def onEvent(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean) {

    logger.info{ _ ++= "onEvent: seq:" ++= sequence.toString ++= "/" + endOfBatch.toString ++= " event: " ++= marketEvent.toString }

    currentSequence = sequence

    marketEvent.trade.foreach{ trade =>
      def updatedPosition = portfolioPosition.add(trade)
      DomainEvents.raise(PositionChangeEvent(updatedPosition))
    }
  }

  def notify(pub: DomainEventsPublisher, event: Event) {
    event match {
      case positionEvent: PositionChangeEvent =>
        logger.info { _ ++= "Position change. seq:" ++= currentSequence.toString ++= " pos:" ++= positionEvent.position.toString }
    }
  }
}
