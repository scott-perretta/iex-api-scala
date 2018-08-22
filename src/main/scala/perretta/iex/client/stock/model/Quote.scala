package perretta.iex.client.stock.model

import org.joda.time.{LocalDate, LocalTime}
import perretta.iex.client.stock.model.CalculationPrice.CalculationPrice
import perretta.iex.client.stock.model.LatestSource.LatestSource
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._

/**
  * The quote of a stock.
  *
  * A quote is the latest price that which a security was traded.
  *
  * @param symbol refers to the stock ticker
  * @param companyName refers to the company name
  * @param primaryExchange refers to the primary listings exchange
  * @param sector refers to the sector of the stock
  * @param calculationPrice refers to the source of the latest price
  * @param open refers to the official open price
  * @param openTime refers to the official listing exchange time for the open
  * @param close refers to the official close price
  * @param closeTime refers to the official listing exchange time for the close
  * @param high refers to the market-wide highest price from the SIP. 15 minute delayed
  * @param low refers to the market-wide lowest price from the SIP. 15 minute delayed
  * @param latestPrice refers to the latest price being the IEX real time price, the
  *                    15 minute delayed market price, or the previous close price
  * @param latestSource refers to the source of latestPrice
  * @param latestTime refers to a human readable time of the latestPrice. The format will vary based on latestSource
  * @param latestUpdate refers to the update time of latestPrice in milliseconds since midnight Jan 1, 1970
  * @param latestVolume refers to the total market volume of the stock
  * @param iexRealtimePrice refers to last sale price of the stock on IEX
  * @param iexRealtimeSize refers to last sale size of the stock on IEX
  * @param iexLastUpdated refers to the last update time of the data in milliseconds since midnight Jan 1, 1970 UTC
  *                       or -1 or 0. If the value is -1 or 0, IEX has not quoted the symbol in the trading day.
  * @param delayedPrice refers to the 15 minute delayed market price during normal market hours 9:30 - 16:00
  * @param delayedPriceTime refers to the time of the delayed market price during normal market hours 9:30 - 16:00
  * @param extendedPrice refers to the 15 minute delayed market price outside
  *                      normal market hours 8:00 - 9:30 and 16:00 - 17:00
  * @param extendedChange is calculated using extendedPrice from calculationPrice.
  * @param extendedChangePercent is calculated using extendedPrice from calculationPrice
  * @param extendedPriceTime refers to the time of the delayed market price outside
  *                          normal market hours 8:00 - 9:30 and 16:00 - 17:00
  * @param previousClose refers to the last closing price
  * @param change is calculated using calculationPrice from previousClose
  * @param changePercent is calculated using calculationPrice from previousClose
  * @param iexMarketPercent refers to IEX’s percentage of the market in the stock
  * @param iexVolume refers to shares traded in the stock on IEX
  * @param avgTotalVolume refers to the 30 day average volume on all markets
  * @param iexBidPrice refers to the best bid price on IEX
  * @param iexBidSize refers to amount of shares on the bid on IEX
  * @param iexAskPrice refers to the best ask price on IEX
  * @param iexAskSize refers to amount of shares on the ask on IEX
  * @param marketCap is calculated in real time using calculationPrice
  * @param peRatio is calculated in real time using calculationPrice
  * @param week52High refers to the adjusted 52 week high
  * @param week52Low refers to the adjusted 52 week low
  * @param ytdChange refers to the price change percentage from start of year to previous close
  */
case class Quote(
  symbol: String,
  companyName: String,
  primaryExchange: String,
  sector: String,
  calculationPrice: CalculationPrice,
  open: Double,
  openTime: LocalTime,
  close: Double,
  closeTime: LocalTime,
  high: Double,
  low: Double,
  latestPrice: Double,
  latestSource: LatestSource,
  latestTime: String,
  latestUpdate: LocalTime,
  latestVolume: Long,
  iexRealtimePrice: Double,
  iexRealtimeSize: Int,
  iexLastUpdated: LocalTime,
  delayedPrice: Double,
  delayedPriceTime: LocalTime,
  extendedPrice: Double,
  extendedChange: Double,
  extendedChangePercent: Double,
  extendedPriceTime: LocalTime,
  previousClose: Double,
  change: Double,
  changePercent: Double,
  iexMarketPercent: Double,
  iexVolume: Long,
  avgTotalVolume: Long,
  iexBidPrice: Double,
  iexBidSize: Int,
  iexAskPrice: Double,
  iexAskSize: Int,
  marketCap: Long,
  peRatio: Double,
  week52High: Double,
  week52Low: Double,
  ytdChange: Double
)

object Quote {

  private case class Fields1To20(
    symbol: String,
    companyName: String,
    primaryExchange: String,
    sector: String,
    calculationPrice: CalculationPrice,
    open: Double,
    openTime: LocalTime,
    close: Double,
    closeTime: LocalTime,
    high: Double,
    low: Double,
    latestPrice: Double,
    latestSource: LatestSource,
    latestTime: String,
    latestUpdate: LocalTime,
    latestVolume: Long,
    iexRealtimePrice: Double,
    iexRealtimeSize: Int,
    iexLastUpdated: LocalTime,
    delayedPrice: Double
  )

  private case class Fields21To40(
    delayedPriceTime: LocalTime,
    extendedPrice: Double,
    extendedChange: Double,
    extendedChangePercent: Double,
    extendedPriceTime: LocalTime,
    previousClose: Double,
    change: Double,
    changePercent: Double,
    iexMarketPercent: Double,
    iexVolume: Long,
    avgTotalVolume: Long,
    iexBidPrice: Double,
    iexBidSize: Int,
    iexAskPrice: Double,
    iexAskSize: Int,
    marketCap: Long,
    peRatio: Double,
    week52High: Double,
    week52Low: Double,
    ytdChange: Double
  )

  private val fields1to20Format: OFormat[Fields1To20] = Json.format[Fields1To20]
  private val fields21to40Format: OFormat[Fields21To40] = Json.format[Fields21To40]

  private val combine: (Fields1To20, Fields21To40) => Quote = {
    case (fields1To20, fields21To40) => Quote(
      fields1To20.symbol,
      fields1To20.companyName,
      fields1To20.primaryExchange,
      fields1To20.sector,
      fields1To20.calculationPrice,
      fields1To20.open,
      fields1To20.openTime,
      fields1To20.close,
      fields1To20.closeTime,
      fields1To20.high,
      fields1To20.low,
      fields1To20.latestPrice,
      fields1To20.latestSource,
      fields1To20.latestTime,
      fields1To20.latestUpdate,
      fields1To20.latestVolume,
      fields1To20.iexRealtimePrice,
      fields1To20.iexRealtimeSize,
      fields1To20.iexLastUpdated,
      fields1To20.delayedPrice,
      fields21To40.delayedPriceTime,
      fields21To40.extendedPrice,
      fields21To40.extendedChange,
      fields21To40.extendedChangePercent,
      fields21To40.extendedPriceTime,
      fields21To40.previousClose,
      fields21To40.change,
      fields21To40.changePercent,
      fields21To40.iexMarketPercent,
      fields21To40.iexVolume,
      fields21To40.avgTotalVolume,
      fields21To40.iexBidPrice,
      fields21To40.iexBidSize,
      fields21To40.iexAskPrice,
      fields21To40.iexAskSize,
      fields21To40.marketCap,
      fields21To40.peRatio,
      fields21To40.week52High,
      fields21To40.week52Low,
      fields21To40.ytdChange
    )
  }

  implicit val format: Format[Quote] = (fields1to20Format ~ fields21to40Format)({
    combine
  }, (quote: Quote) => (
    Fields1To20(
      quote.symbol,
      quote.companyName,
      quote.primaryExchange,
      quote.sector,
      quote.calculationPrice,
      quote.open,
      quote.openTime,
      quote.close,
      quote.closeTime,
      quote.high,
      quote.low,
      quote.latestPrice,
      quote.latestSource,
      quote.latestTime,
      quote.latestUpdate,
      quote.latestVolume,
      quote.iexRealtimePrice,
      quote.iexRealtimeSize,
      quote.iexLastUpdated,
      quote.delayedPrice
    ),
    Fields21To40(
      quote.delayedPriceTime,
      quote.extendedPrice,
      quote.extendedChange,
      quote.extendedChangePercent,
      quote.extendedPriceTime,
      quote.previousClose,
      quote.change,
      quote.changePercent,
      quote.iexMarketPercent,
      quote.iexVolume,
      quote.avgTotalVolume,
      quote.iexBidPrice,
      quote.iexBidSize,
      quote.iexAskPrice,
      quote.iexAskSize,
      quote.marketCap,
      quote.peRatio,
      quote.week52High,
      quote.week52Low,
      quote.ytdChange
    )
  ))
  
}