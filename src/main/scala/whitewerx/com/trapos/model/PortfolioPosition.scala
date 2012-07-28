package whitewerx.com.trapos.model

import scala.collection.mutable.{HashMap => MutableHashMap}

/**
 * @author ludo
 */

class PortfolioPosition(val positionFactory: PositionFactory) {
  private val positions = new MutableHashMap[CurrencyPair, Position]

  def add(trade: Trade): Position = {
    val position = findPositionFor(trade.currencyPair)
    val updatedPosition = position.add(trade)
    positions.put(trade.currencyPair, updatedPosition)
    updatedPosition
  }

  /**
   * Finds the current open position or creates a flat one
   * if none exists.
   *
   * @param currencyPair for the desired position.
   * @return the position.
   */
  private def findPositionFor(currencyPair: CurrencyPair): Position = {
    val p = positions.get(currencyPair)
    p.getOrElse({
      val position = positionFactory.createFlatPositionFor(currencyPair)
      positions.put(currencyPair, position)
      position
    })
  }
}
