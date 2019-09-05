package com.mobile

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class WebSocketSimulation extends Simulation with TestConfig {


  val httpProtocol = http
    .baseUrl(s"$baseUrl") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  var randomDeviceId = Iterator.continually(Map("deviceId" -> ( UUID.randomUUID().toString )))

  val scn = scenario("WebSocket Connection Scenario") // A scenario is a chain of requests and pauses
    .feed(randomDeviceId)
    .exec(ws("Connect WS").connect(s"$wsUrl/ws/connect/"+"${deviceId}")
      .await(5 seconds)(
        ws.checkTextMessage("greetingMessage")
          .matching(jsonPath("$.kind").is("ConnectionBegin")))
      /*.await(5 seconds)(
        ws.checkTextMessage("availabilityMessage")
          .matching(jsonPath("$.kind").is("ConnectionReady")))
      .await(5 seconds)(
        ws.checkTextMessage("statusMessage")
          .matching(jssonPath("$.kind").is("ConnectionStatus")))*/
    ).pause(duration.getSeconds+5 seconds)
    .exec(ws("Close WS").close)


  setUp(scn.inject(
    nothingFor(5 seconds),
    constantUsersPerSec(user) during (duration.getSeconds seconds),
    nothingFor(30 seconds)
  ).protocols(httpProtocol))
    .maxDuration(5+duration.getSeconds*2+5+30+1 seconds)
}
