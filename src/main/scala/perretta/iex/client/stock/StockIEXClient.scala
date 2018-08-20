package perretta.iex.client.stock

import perretta.iex.client.{ClientConstants, IEXClient}
import perretta.iex.client.stock.model.TickerSymbol
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
  def getPrice(tickerSymbol: TickerSymbol): Future[Double] = wsClient
    .url(baseUrl + tickerSymbol.symbol + "/price")
    .get()
    .map(response => Json.parse(response.body).as[Double])

}
