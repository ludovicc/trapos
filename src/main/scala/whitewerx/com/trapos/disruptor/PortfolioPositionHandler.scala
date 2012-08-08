package whitewerx.com.trapos.disruptor

import whitewerx.com.trapos.model.{Trade, PortfolioPosition}
import whitewerx.com.trapos.model.event.{DomainEvents, EventHandler, PositionChangeEvent}

/**
 * @author ludo
 */

object PortfolioPositionHandler {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

}

class PortfolioPositionHandler(private val portfolioPosition: PortfolioPosition) extends EventHandler[PositionChangeEvent] {
  import PortfolioPositionHandler.logger
  import PortfolioPositionHandler.formatter._

  private var currentSequence: Long = 0L

  def onEvent(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean) {

    logger.info{ _ ++= "onEvent: seq:" ++= sequence.toString ++= "/" + endOfBatch.toString ++= " event: " ++= marketEvent.toString }

    currentSequence = sequence

    marketEvent.trade.foreach{trade =>
      def updatedPosition = portfolioPosition.add(trade)
      DomainEvents.raise(PositionChangeEvent(updatedPosition))
    }
  }

  /**
   * Log the positions as they are changed, in the real world this might
   * notify something else.
   */
  def handle(event: PositionChangeEvent) {
    logger.info { _ ++= "Position change. seq:" ++= currentSequence.toString ++= " pos:" ++= event.position.toString }
  }

}
