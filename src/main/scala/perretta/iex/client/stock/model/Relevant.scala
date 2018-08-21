package perretta.iex.client.stock.model

import play.api.libs.json.{Format, Json}

/**
  * A collection of relevant ticker symbols for a stock. These symbols may or may not be peer tickers.
  * @param peers Flag that indicates when [[symbols]] contains peer tickers.
  * @param symbols Collection of ticker symbols.
  */
case class Relevant(peers: Boolean, symbols: Seq[String])

object Relevant {

  implicit val format: Format[Relevant] = Json.format[Relevant]

}

