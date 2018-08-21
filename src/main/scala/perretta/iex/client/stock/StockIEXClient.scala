package perretta.iex.client.stock

import perretta.iex.client.{ClientConstants, IEXClient}
import perretta.iex.client.stock.model._
import play.api.libs.json.Json
import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Client handling all HTTP requests for the stocks endpoint.
  */
case class StockIEXClient(override protected val wsClient: StandaloneWSClient)(implicit val executionContext: ExecutionContext) extends IEXClient {

  override protected def baseUrl: String = ClientConstants.BaseUrlPrefix + "/stock/"

  /**
    * Gets the market price for a ticker symbol.
    * @param symbol The stock's ticker symbol.
    * @return A single number, being the IEX real time price, the 15 minute
    *         delayed market price, or the previous close price, is returned.
    */
  def getPrice(symbol: String): Future[Double] = wsClient
    .url(baseUrl + symbol + "/price")
    .get()
    .map(response => Json.parse(response.body).as[Double])

  /**
    * Gets the location of a company's logo.
    * @param symbol The stock's ticker symbol.
    * @return Location of the company logo.
    */
  def getLogo(symbol: String): Future[Logo] = wsClient
    .url(baseUrl + symbol + "/logo")
    .get()
    .map(response => Json.parse(response.body).as[Logo])

  /**
    * Gets a collection of peer ticker symbols.
    *
    * Stocks may often have a peer group associated with it. A peer group is a collection of stocks
    * that are in the same industry sector, and are of similar size, as a particular stock.
    *
    * @param symbol The stock's ticker symbol.
    * @return An array of peer tickers as defined by IEX. This is not intended to represent
    *         a definitive or accurate list of peers, and is subject to change at any time.
    */
  def getPeers(symbol: String): Future[Seq[String]] = wsClient
    .url(baseUrl + symbol + "/peers")
    .get()
    .map(response => Json.parse(response.body).as[Seq[String]])

  /**
    * Gets a stock's relevant symbols.
    * @param symbol The stock's ticker symbol.
    * @return Similar to the peers endpoint, except this will return most active market symbols
    *         when peers are not available. If the symbols returned are not peers, the peers key
    *         will be false. This is not intended to represent a definitive or accurate list of
    *         peers, and is subject to change at any time.
    */
  def getRelevant(symbol: String): Future[Relevant] = wsClient
    .url(baseUrl + symbol + "/relevant")
    .get()
    .map(response => Json.parse(response.body).as[Relevant])

}
