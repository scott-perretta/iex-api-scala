package perretta.iex.client.stock

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import org.f100ded.play.fakews._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AsyncFunSpec, BeforeAndAfterAll, Matchers}
import perretta.iex.client.ClientConstants
import perretta.iex.client.stock.model.TickerSymbol

import scala.concurrent.duration._
import scala.concurrent.Await

class StockIEXClientSpec extends AsyncFunSpec with Matchers with BeforeAndAfterAll with ScalaFutures {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(60.seconds)

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materialize: Materializer = ActorMaterializer()

  def makeFakeStockIEXClient(symbol: TickerSymbol, endpoint: String, response: String): StockIEXClient = {
    val requestUrl = s"${ClientConstants.BaseUrlPrefix}/stock/${symbol.symbol}/$endpoint"
    val wsClient = StandaloneFakeWSClient {
      case _ @ GET(url"$url") =>
        url shouldBe requestUrl
        Ok(response)
    }
    StockIEXClient(wsClient)
  }

  describe("getPrice") {
    it("should get the price of a stock.") {
      val expected: Double = 1000.0
      val symbol = TickerSymbol("NVDA")
      val client = makeFakeStockIEXClient(symbol, "price", expected.toString)
      client.getPrice(symbol).map(_ shouldEqual expected)
    }
  }

  override def afterAll() {
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

}
