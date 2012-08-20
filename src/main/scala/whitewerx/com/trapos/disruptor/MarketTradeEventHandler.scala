package whitewerx.com.trapos.disruptor

import whitewerx.com.trapos.translators.TradeTranslator
import whitewerx.com.trapos.model.Trade
import com.lmax.disruptor.EventHandler

/**
 * @author ludo
 */

object MarketTradeEventHandler {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

}

class MarketTradeEventHandler(translator: TradeTranslator) extends EventHandler[MarketEvent] {

  import MarketTradeEventHandler.logger
  import MarketTradeEventHandler.formatter._

  def onEvent(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean) {
    val delimitedTrade = marketEvent.message

    val trade = translator.unapply(delimitedTrade)
    trade.foreach(process(marketEvent, sequence, endOfBatch))
  }

  private def process(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean)(trade: Trade) {
    marketEvent.accept(trade)
    logger.info{ _ ++= "onEvent: seq:" ++= sequence.toString ++= "/" + endOfBatch ++= " event: " ++= marketEvent.toString }
  }
}