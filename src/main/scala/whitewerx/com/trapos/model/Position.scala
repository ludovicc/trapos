/**
 * Created with IntelliJ IDEA.
 * User: ludo
 * Date: 7/17/12
 * Time: 12:14 AM
 * To change this template use File | Settings | File Templates.
 */

case class Position protected(ccy1Amount: Amount, ccy2Amount: Amount,
                    ccy1EquivalentInPNLCurrency: Amount, ccy2EquivalentInPNLCurrency: Amount,
                    currencyPair: CurrencyPair, pnlCurrency: Currency) {

  override def toString: String = {
      "Position [ccy1Amount=" + ccy1Amount + ", ccy2Amount=" + ccy2Amount
        + ", ccy1EquivalentInPNLCurrency=" + ccy1EquivalentInPNLCurrency + ", ccy2EquivalentInPNLCurrency=" + ccy2EquivalentInPNLCurrency
        + ", currencyPair=" + currencyPair + ", pnlCurrency=" + pnlCurrency + "]"
  }

  /**
     * Adds the trade to the position.
     *
     * Note: This is incomplete as it assumes the rate
     * is quoted in terms of USD.  E.g. EURUSD, GBPUSD, etc.
     *
     * @param trade
     */
    def add(trade: Trade): Position {
      val ccy2AmountDelta: Amount = trade.getQuoteAmount
      if (trade.isPurchase) {
        new Position(
          ccy1Amount = ccy1Amount.add(trade.getBaseAmount),
          ccy2Amount = ccy2Amount.subtract(ccy2AmountDelta),
          ccy1EquivalentInPNLCurrency = ccy1EquivalentInPNLCurrency.add(ccy2AmountDelta),
          ccy2EquivalentInPNLCurrency = ccy2Amount,
          currencyPair, pnlCurrency)
      }
      else {
        new Position(
          ccy1Amount = ccy1Amount.subtract(trade.getBaseAmount),
          ccy2Amount = ccy2Amount.add(ccy2AmountDelta),
          ccy1EquivalentInPNLCurrency = ccy1EquivalentInPNLCurrency.subtract(ccy2AmountDelta),
          ccy2EquivalentInPNLCurrency = ccy2Amount,
          currencyPair, pnlCurrency)
      }
    }
}

object Position {
  /**
     * Note this assumes the PNL currency is always the quote currency in a real
     * system this would not be correct.
     *
     * For example if the PNL currency is USD, and the position is USDCAD then
     * this would be incorrect.
     *
     * @param currencyPair
       * for the flat position.
     */
    def createFlatPositionFor(currencyPair: CurrencyPair): Position = {
      val ccy1Amount: Amount = Amount(0, currencyPair.base)
      val ccy2Amount: Amount = Amount(0, currencyPair.quote)
      val ccy1EqvAmount: Amount = ccy2Amount
      val ccy2EqvAmount: Amount = ccy2Amount
      return new Position(ccy1Amount, ccy2Amount, ccy1EqvAmount, ccy2EqvAmount, currencyPair, currencyPair.quote)
    }
  }