package perretta.iex.client.stock.model

import play.api.libs.json.{Reads, Writes}

/**
  * All calculation prices supported by the IEX API.
  */
object CalculationPrice extends Enumeration {
  type CalculationPrice = Value

  val Tops = Value("tops")
  val Sip = Value("sip")
  val PreviousClose = Value("previousclose")
  val Close = Value("close")

  implicit val reads: Reads[CalculationPrice] = Reads.enumNameReads(CalculationPrice)
  implicit val writes: Writes[CalculationPrice] = Writes.enumNameWrites
}
