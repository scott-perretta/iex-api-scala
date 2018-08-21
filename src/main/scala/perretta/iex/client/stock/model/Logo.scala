package perretta.iex.client.stock.model

import play.api.libs.json.{Format, Json}

/**
  * A company's logo
  * @param url Location of the logo, provided via Google's API.
  */
case class Logo(url: String)

object Logo {

  implicit val format: Format[Logo] = Json.format[Logo]

}
