package perretta.iex.client.stock

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import org.f100ded.play.fakews._
import org.joda.time.LocalDate
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

  lazy val aaplSymbol = "AAPL"

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
      val client = makeFakeStockIEXClient(aaplSymbol, "price", expected.toString)
      client.getPrice(aaplSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getLogo") {
    it("should get the logo of a company.") {
      val expected: Logo = Logo("https://storage.googleapis.com/iex/api/logos/AAPL.png")
      val fakeResponse = """{"url":"https://storage.googleapis.com/iex/api/logos/AAPL.png"}"""
      val client = makeFakeStockIEXClient(aaplSymbol, "logo", fakeResponse)
      client.getLogo(aaplSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getPeers") {
    it("should get the peer group for a stock.") {
      val expected: Seq[String] = Seq("FB", "GOOGL", "AMD")
      val fakeResponse = """["FB", "GOOGL", "AMD"]"""
      val client = makeFakeStockIEXClient(aaplSymbol, "peers", fakeResponse)
      client.getPeers(aaplSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getRelevant") {
    it("should get the relevant ticker symbols for a stock.") {
      val expected: Relevant = Relevant(peers = false, Seq("FB", "GOOGL", "AMD"))
      val fakeResponse = """{"peers":false,"symbols":["FB", "GOOGL", "AMD"]}"""
      val client = makeFakeStockIEXClient(aaplSymbol, "relevant", fakeResponse)
      client.getRelevant(aaplSymbol).map(_ shouldEqual expected)
    }
  }

  describe("getSplits") {
    it("should get all stock splits that occurred during a given date range.") {
      val expected: Seq[Split] = Seq(
        Split(
          exDate = new LocalDate("2014-06-09"),
          declaredDate = new LocalDate("2014-04-23"),
          recordDate = new LocalDate("2014-06-02"),
          paymentDate = new LocalDate("2014-06-06"),
          ratio = 0.142857,
          toFactor = 7,
          forFactor = 1
        ),
        Split(
          exDate = new LocalDate("2013-06-09"),
          declaredDate = new LocalDate("2013-04-23"),
          recordDate = new LocalDate("2013-06-02"),
          paymentDate = new LocalDate("2013-06-06"),
          ratio = 0.234567,
          toFactor = 3,
          forFactor = 2
        )
      )
      val fakeResponse =
        """[
             {
               "exDate":"2014-06-09",
               "declaredDate":"2014-04-23",
               "recordDate":"2014-06-02",
               "paymentDate":"2014-06-06",
               "ratio":0.142857,
               "toFactor":7,
               "forFactor":1
             },
             {
               "exDate":"2013-06-09",
               "declaredDate":"2013-04-23",
               "recordDate":"2013-06-02",
               "paymentDate":"2013-06-06",
               "ratio":0.234567,
               "toFactor":3,
               "forFactor":2
             }
           ]""".stripMargin
      val client = makeFakeStockIEXClient(aaplSymbol, s"splits/${DateRange.FiveYear}", fakeResponse)
      client.getSplits(aaplSymbol, DateRange.FiveYear).map(_ shouldEqual expected)
    }
  }

  describe("getQuote") {
    val requestUrl = s"${ClientConstants.BaseUrlPrefix}/stock/$aaplSymbol/quote"
    val wsClient = StandaloneFakeWSClient {
      case _ @ GET(url"$url?displayPercent=true") =>
        url should startWith(requestUrl)
        Ok("""{"symbol":"AAPL","companyName":"Apple Inc.","primaryExchange":"Nasdaq Global Select","sector":"Technology","calculationPrice":"close","open":216.62,"openTime":1534858200677,"close":215.04,"closeTime":1534881600191,"high":217.19,"low":214.025,"latestPrice":215.04,"latestSource":"Close","latestTime":"August 21, 2018","latestUpdate":1534881600191,"latestVolume":25092308,"iexRealtimePrice":215.12,"iexRealtimeSize":1,"iexLastUpdated":1534881597014,"delayedPrice":215.06,"delayedPriceTime":1534881600217,"extendedPrice":214.46,"extendedChange":-0.58,"extendedChangePercent":-0.27,"extendedPriceTime":1534885197225,"previousClose":215.46,"change":-0.42,"changePercent":-0.19499999999999998,"iexMarketPercent":2.392,"iexVolume":600208,"avgTotalVolume":25766068,"iexBidPrice":0,"iexBidSize":0,"iexAskPrice":0,"iexAskSize":0,"marketCap":1038627287040,"peRatio":20.76,"week52High":219.18,"week52Low":149.16,"ytdChange":26.07317189476242}""")
      case _ @ GET(url"$url?displayPercent=false") =>
        url should startWith(requestUrl)
        Ok("""{"symbol":"AAPL","companyName":"Apple Inc.","primaryExchange":"Nasdaq Global Select","sector":"Technology","calculationPrice":"close","open":216.62,"openTime":1534858200677,"close":215.04,"closeTime":1534881600191,"high":217.19,"low":214.025,"latestPrice":215.04,"latestSource":"Close","latestTime":"August 21, 2018","latestUpdate":1534881600191,"latestVolume":25092308,"iexRealtimePrice":215.12,"iexRealtimeSize":1,"iexLastUpdated":1534881597014,"delayedPrice":215.06,"delayedPriceTime":1534881600217,"extendedPrice":214.46,"extendedChange":-0.58,"extendedChangePercent":-0.0027,"extendedPriceTime":1534885197225,"previousClose":215.46,"change":-0.42,"changePercent":-0.00195,"iexMarketPercent":0.02392,"iexVolume":600208,"avgTotalVolume":25766068,"iexBidPrice":0,"iexBidSize":0,"iexAskPrice":0,"iexAskSize":0,"marketCap":1038627287040,"peRatio":20.76,"week52High":219.18,"week52Low":149.16,"ytdChange":0.2607317189476242}""")
    }
    val client = StockIEXClient(wsClient)
    it("should get the quote of a stock, with percentages when flag is enabled.") {
      val expectedYTDChange = 26.07317189476242
      client.getQuote(aaplSymbol, true).map(response => response.ytdChange shouldEqual expectedYTDChange)
    }
    it("should get the quote of a stock, without percentages when flag is disabled.") {
      val expectedYTDChange = 0.2607317189476242
      client.getQuote(aaplSymbol, false).map(response => response.ytdChange shouldEqual expectedYTDChange)
    }
  }

  override def afterAll() {
    Await.result(actorSystem.terminate(), Duration.Inf)
  }

}
