package whitewerx.com.trapos.model

case class Position (ccy1Amount: Amount, ccy2Amount: Amount,
                    ccy1EquivalentInPNLCurrency: Amount, ccy2EquivalentInPNLCurrency: Amount,
                    currencyPair: CurrencyPair, pnlCurrency: Currency) {

  override def toString: String = {
// XXX Scala 2.10
//      "Position [ccy1Amount=${ccy1Amount}, ccy2Amount=${ccy2Amount}, ccy1EquivalentInPNLCurrency=${ccy1EquivalentInPNLCurrency}, ccy2EquivalentInPNLCurrency=${ccy2EquivalentInPNLCurrency}, currencyPair=${currencyPair}, pnlCurrency=${pnlCurrency}]"
      "Position [ccy1Amount=" + ccy1Amount + ", ccy2Amount=" + ccy2Amount + ", ccy1EquivalentInPNLCurrency=" + ccy1EquivalentInPNLCurrency +
        ", ccy2EquivalentInPNLCurrency=" + ccy2EquivalentInPNLCurrency + ", currencyPair=" + currencyPair + ", pnlCurrency=" + pnlCurrency + "]"
  }

  /**
     * Adds the trade to the position.
     *
     * Note: This is incomplete as it assumes the rate
     * is quoted in terms of USD.  E.g. EURUSD, GBPUSD, etc.
     *
     * @param trade
     */
    def add(trade: Trade): Position = {
      val ccy2AmountDelta = trade.quoteAmount
      trade match {
        case _: Purchase =>
          val updatedCcy2 = this.ccy2Amount - ccy2AmountDelta
          new Position(
            ccy1Amount = this.ccy1Amount + trade.baseAmount,
            ccy2Amount = updatedCcy2,
            ccy1EquivalentInPNLCurrency = this.ccy1EquivalentInPNLCurrency + ccy2AmountDelta,
            ccy2EquivalentInPNLCurrency = updatedCcy2,
            currencyPair = this.currencyPair,
            pnlCurrency = this.pnlCurrency)
        case _: Sell =>
          val updatedCcy2 = this.ccy2Amount + ccy2AmountDelta
          new Position(
            ccy1Amount = this.ccy1Amount - trade.baseAmount,
            ccy2Amount = updatedCcy2,
            ccy1EquivalentInPNLCurrency = this.ccy1EquivalentInPNLCurrency - ccy2AmountDelta,
            ccy2EquivalentInPNLCurrency = updatedCcy2,
            currencyPair = this.currencyPair,
            pnlCurrency = this.pnlCurrency)
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