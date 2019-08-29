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
      .await(10 seconds)(
        ws.checkTextMessage("greetingMessage")
          .matching(jsonPath("$.kind").is("ConnectionBegin")))
      /*.await(5 seconds)(
        ws.checkTextMessage("availabilityMessage")
          .matching(jsonPath("$.kind").is("ConnectionReady")))
      .await(5 seconds)(
        ws.checkTextMessage("statusMessage")
          .matching(jsonPath("$.kind").is("ConnectionStatus")))*/
    ).pause(30 seconds)
    .exec(ws("Close WS").close)


  setUp(scn.inject(
    nothingFor(1 seconds),
    constantUsersPerSec(500) during (25 seconds),
    nothingFor(10 seconds)
  ).protocols(httpProtocol))
    .maxDuration(55 seconds)
}
