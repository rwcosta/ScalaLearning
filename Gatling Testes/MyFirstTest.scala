package computerdatabase

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyFirstTest extends Simulation {
    val httpProtocol = http.baseUrl("https://reqres.in")

    val header = Map(
        "accept" -> "*/*",
        "accept-encoding" -> "gzip, deflate, br",
        "accept-language" -> "en-US,en;q=0.9,pt;q=0.8",
        "cache-control" -> "no-cache",
        "content-type" -> "application/json",
        "pragma" -> "no-cache",
        "referer" -> "https://reqres.in/"
    )

    val scn = scenario("MyFirstTest")
        .exec(http("GET_USERS_01")
            .get("/api/users?page=2")
            .headers(header))
        .pause(3)

        .exec(http("GET_USER_01")
            .get("/api/users/2")
            .headers(header))
        .pause(2)

        .exec(http("POST_USER_01")
            .post("/api/users")
            .body(StringBody("""{ "name": "morpheus", "job": "leader" }""")).asJson
            .check(status.is(201)))

        .exec(http("GET_DELAYED_RESPONSE")
            .get("/api/users?page=2")
            .headers(header))
        .pause(3)

        setUp(scn
            .inject(
                atOnceUsers(10),
                nothingFor(5 seconds),
                rampUsers(10) during (5 seconds)
            ))
            .protocols(httpProtocol)
}
