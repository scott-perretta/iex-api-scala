package perretta.iex.client.stock.model

import play.api.libs.json.{Reads, Writes}

/**
  * All latest sources supported by the IEX API.
  */
object LatestSource extends Enumeration {
  type LatestSource = Value

  val IEXRealTimePrice = Value("IEX real time price")
  val FifteenMinuteDelayedPrice = Value("15 minute delayed price")
  val Close = Value("Close")
  val PreviousClose = Value("Previous close")

  implicit val reads: Reads[LatestSource] = Reads.enumNameReads(LatestSource)
  implicit val writes: Writes[LatestSource] = Writes.enumNameWrites
}
