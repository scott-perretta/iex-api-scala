package perretta.iex.client.stock

import perretta.iex.client.stock.model.DateRange.DateRange
import perretta.iex.client.{ClientConstants, IEXClient}
import perretta.iex.client.stock.model._
import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Client handling all HTTP requests for the stocks endpoint.
  */
case class StockIEXClient(
  override protected val wsClient: StandaloneWSClient
)(implicit val executionContext: ExecutionContext) extends IEXClient {

  override protected def baseUrl: String = ClientConstants.BaseUrlPrefix + "/stock/"

  /**
    * Gets the market price for a ticker symbol.
    * @param symbol the stock's ticker symbol
    * @return a single number, being the IEX real time price, the 15 minute
    *         delayed market price, or the previous close price, is returned
    */
  def getPrice(symbol: String): Future[Double] = wsClientGetRequest[Double](symbol + "/price")

  /**
    * Gets the location of a company's logo.
    * @param symbol the stock's ticker symbol
    * @return location of the company logo
    */
  def getLogo(symbol: String): Future[Logo] = wsClientGetRequest[Logo](symbol + "/logo")

  /**
    * Gets a collection of peer ticker symbols.
    *
    * A stock can have a peer group associated with itself. A peer group is a collection of ticker
    * symbols that are in the same industry sector, and are of similar size, as a particular stock.
    *
    * @param symbol the stock's ticker symbol
    * @return an array of peer tickers as defined by IEX. This is not intended to represent
    *         a definitive or accurate list of peers, and is subject to change at any time
    */
  def getPeers(symbol: String): Future[Seq[String]] = wsClientGetRequest[Seq[String]](symbol + "/peers")

  /**
    * Gets a stock's relevant symbols.
    * @param symbol the stock's ticker symbol
    * @return similar to the peers endpoint, except this will return most active market symbols
    *         when peers are not available. If the symbols returned are not peers, the peers key
    *         will be false. This is not intended to represent a definitive or accurate list of
    *         peers, and is subject to change at any time
    */
  def getRelevant(symbol: String): Future[Relevant] = wsClientGetRequest[Relevant](symbol + "/relevant")

  /**
    * Gets all stock split occurrences during a given date range.
    * @param symbol the stock's ticker symbol
    * @param dateRange a given [[DateRange]] to look for stock splits
    * @return a collection of stock splits
    */
  def getSplits(symbol: String, dateRange: DateRange): Future[Seq[Split]] = {
    val endpoint = symbol + "/splits/" + dateRange.toString
    wsClientGetRequest[Seq[Split]](endpoint)
  }

  /**
    * Gets the quote of a stock.
    * @param symbol the stock's ticker symbol
    * @param displayPercent a flag indicating whether or not to show percent values for applicable fields
    * @return a quote for a stock
    */
  def getQuote(symbol: String, displayPercent: Boolean): Future[Quote] = {
    val endpoint = symbol + "/quote"
    val queryParameters: Seq[(String, String)] = Seq(("displayPercent", displayPercent.toString))
    wsClientGetRequest[Quote](endpoint, queryParameters)
  }

}
