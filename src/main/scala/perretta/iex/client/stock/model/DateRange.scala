package perretta.iex.client.stock.model

/**
  * All date ranges supported by the IEX API.
  */
object DateRange extends Enumeration {
  type DateRange = Value

  val FiveYear = Value("5y")
  val TwoYear = Value("2y")
  val OneYear = Value("1y")
  val YearToDate = Value("ytd")
  val SixMonth = Value("6m")
  val ThreeMonth = Value("3m")
  val OneMonth = Value("1m")
}
