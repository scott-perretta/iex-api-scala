package perretta.iex.client.stock.model

import org.joda.time.LocalDate
import play.api.libs.json.{Format, Json}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._

/**
  * A stock split.
  *
  * A split is when a company issues new shares to existing shareholders in proportion to their current holdings.
  *
  * @param exDate refers to the split ex-date
  * @param declaredDate refers to the split declaration date
  * @param recordDate refers to the split record date
  * @param paymentDate refers to the split payment date
  * @param ratio refers to the split ratio. The split ratio is an inverse of the number of shares that a holder of
  *              the stock would have after the split divided by the number of shares that the holder had before.
  *
  *              For example: Split ratio of .5 = 2 for 1 split.
  * @param toFactor To factor of the split. Used to calculate the split ratio forfactor/tofactor = ratio (eg ½ = 0.5)
  * @param forFactor For factor of the split. Used to calculate the split ratio forfactor/tofactor = ratio (eg ½ = 0.5)
  */
case class Split(
  exDate: LocalDate,
  declaredDate: LocalDate,
  recordDate: LocalDate,
  paymentDate: LocalDate,
  ratio: Double,
  toFactor: Int,
  forFactor: Int
)

object Split {

  implicit val format: Format[Split] = Json.format[Split]

}