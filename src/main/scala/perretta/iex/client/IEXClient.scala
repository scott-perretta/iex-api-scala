package perretta.iex.client

import play.api.libs.json.{Json, Reads}
import play.api.libs.ws.StandaloneWSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Defines a basic abstract configuration for IEX API client implementations.
  */
trait IEXClient {

  protected def wsClient: StandaloneWSClient

  protected def baseUrl: String

  /**
    * Helper function for [[StandaloneWSClient]] GET requests.
    * @param endpoint the endpoint the request will hit
    * @param reads json deserializer for type [[T]]
    * @tparam T type that the response body will be parsed as
    * @return a parsed response body
    */
  protected def wsClientGetRequest[T](
    endpoint: String
  )(implicit reads: Reads[T], executionContext: ExecutionContext): Future[T] = wsClient
    .url(baseUrl + endpoint)
    .get()
    .map(response => Json.parse(response.body).as[T])

}
