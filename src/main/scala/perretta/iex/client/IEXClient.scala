package perretta.iex.client

import play.api.libs.ws.StandaloneWSClient

/**
  * Defines a basic abstract configuration for IEX API client implementations.
  */
trait IEXClient {

  protected def wsClient: StandaloneWSClient

  protected def baseUrl: String

}
