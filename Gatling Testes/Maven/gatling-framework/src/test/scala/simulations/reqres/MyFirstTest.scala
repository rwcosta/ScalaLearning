package reqres

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class MyFirstTest extends Simulation {
    private def getProperty(name: String, defaultValue: String): String = {
        Option(System.getenv(name))
            .orElse(Option(System.getProperty(name)))
            .getOrElse(defaultValue)
    }

    def users: Int = getProperty("users", "1").toInt
    def rampDuration: Int = getProperty("ramp_duration", "10").toInt
    def rampPerSecRate: Int = getProperty("ramp_per_sec_rate", s"${users+10}").toInt
    def constantUserDuration: Int = getProperty("const_user_duration", "10").toInt
    def heavisideMult: Int = getProperty("heaviside_mult", "2").toInt
    def heavisideDuration: Int = getProperty("heaviside_duration", "10").toInt

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
                atOnceUsers(users),
                nothingFor(3 seconds),
                heavisideUsers(users*heavisideMult) during(heavisideDuration),
                nothingFor(5 seconds),
                constantUsersPerSec(users) during(constantUserDuration),
                nothingFor(5 seconds),
                rampUsersPerSec(users) to rampPerSecRate during(rampDuration)
            ))
            .assertions(
                forAll.responseTime.max.lte(150),
                global.successfulRequests.percent.gte(95)
            )
            .protocols(httpProtocol)
}
