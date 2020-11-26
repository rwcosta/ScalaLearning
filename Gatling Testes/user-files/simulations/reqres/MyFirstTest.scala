package reqres

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyFirstTest extends Simulation {
    val httpProtocol = http.baseUrl("https://reqres.in")

    val header = Map(
        "content-type" -> "application/json"
    )

    val scn = scenario("MyFirstTest")
        .exec(http("GET_USERS_01")
            .get("/api/users?page=2")
            .headers(header))
        .pause(2)

        .exec(http("GET_USER_01")
            .get("/api/users/2")
            .headers(header))
        .pause(2)

        .exec(http("PUT_USER_01")
            .put("/api/users/2")
            .body(StringBody(s""" {"name": "morpheus", "job": "zion resident", "updatedAt": "${java.time.LocalDate.now}" } """))
            .check(status.is(200)))
        .pause(2)

        .exec(http("POST_USER_01")
            .post("/api/users")
            .body(StringBody("""{ "name": "morpheus", "job": "leader" }""")).asJson
            .check(status.is(201)))
        .pause(2)

        .exec(http("GET_DELAYED_RESPONSE")
            .get("/api/users?page=2")
            .headers(header))
        .pause(2)

        setUp(scn
            .inject(
                atOnceUsers(10),
                nothingFor(3 seconds),
                heavisideUsers(50) during(10 seconds),
                nothingFor(5 seconds),
                constantUsersPerSec(10) during(5 seconds),
                nothingFor(5 seconds),
                rampUsersPerSec(10) to 15 during(5 seconds)
            ))
            .assertions(
                forAll.responseTime.max.lte(150),
                global.successfulRequests.percent.gte(95)
            )
            .protocols(httpProtocol)
}
