package perretta.iex.client.stock

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import org.f100ded.play.fakews._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AsyncFunSpec, BeforeAndAfterAll, Matchers}
import perretta.iex.client.ClientConstants
import perretta.iex.client.stock.model._

import scala.concurrent.duration._
import scala.concurrent.Await

class StockIEXClientSpec extends AsyncFunSpec with Matchers with BeforeAndAfterAll with ScalaFutures {

  override implicit val patienceConfig: PatienceConfig = PatienceConfig(60.seconds)

  implicit val actorSystem: ActorSystem = ActorSystem()
  implicit val materialize: Materializer = ActorMaterializer()

  lazy val nvdaSymbol = "NVDA"

  def makeFakeStockIEXClient(symbol: String, endpoint: String, response: String): StockIEXClient = {
    val requestUrl = s"${ClientConstants.BaseUrlPrefix}/stock/$symbol/$endpoint"
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
      val client = makeFakeStockIEXClient(nvdaSymbol, "price", expected.toString)
      client.getPrice(nvdaSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getLogo") {
    it("should get the logo of a company.") {
      val expected: Logo = Logo("https://storage.googleapis.com/iex/api/logos/NVDA.png")
      val fakeResponse = """{"url":"https://storage.googleapis.com/iex/api/logos/NVDA.png"}"""
      val client = makeFakeStockIEXClient(nvdaSymbol, "logo", fakeResponse)
      client.getLogo(nvdaSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getPeers") {
    it("should get the peer group for a stock.") {
      val expected: Seq[String] = Seq("FB", "AAPL", "AMD")
      val fakeResponse = """["FB", "AAPL", "AMD"]"""
      val client = makeFakeStockIEXClient(nvdaSymbol, "peers", fakeResponse)
      client.getPeers(nvdaSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getRelevant") {
    it("should get the relevant ticker symbols for a stock.") {
      val expected: Relevant = Relevant(peers = false, Seq("FB", "AAPL", "AMD"))
      val fakeResponse = """{"peers":false,"symbols":["FB", "AAPL", "AMD"]}"""
      val client = makeFakeStockIEXClient(nvdaSymbol, "relevant", fakeResponse)
      client.getRelevant(nvdaSymbol).map(_ shouldEqual expected)
    }
  }

  override def afterAll() {
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

}
