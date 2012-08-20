package whitewerx.com.trapos.disruptor

import whitewerx.com.trapos.translators.RateTranslator
import whitewerx.com.trapos.model.Rate
import com.lmax.disruptor.EventHandler

/**
 * @author ludo
 */

object MarketRateEventHandler {

  val (logger, formatter) = ZeroLoggerFactory.newLogger(this)

}

class MarketRateEventHandler(translator: RateTranslator) extends EventHandler[MarketEvent] {

  import MarketRateEventHandler.logger
  import MarketRateEventHandler.formatter._

  def onEvent(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean) {
    val delimitedRate = marketEvent.message

    val rate = translator.unapply(delimitedRate)

    rate.foreach(process(marketEvent, sequence, endOfBatch))
  }

  private def process(marketEvent: MarketEvent, sequence: Long, endOfBatch: Boolean)(rate: Rate) {
    marketEvent.accept(rate)
    logger.info{ _ ++= "onEvent: seq:" ++= sequence.toString ++= "/" + endOfBatch ++= " event: " ++= marketEvent.toString }
  }

}
