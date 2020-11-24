

import io.gatling.core.Predef._
import io.gatlinh.http.Predef._

class MyFirstTest extends Simulation {
    val httpProtocol = http
        .baseURL("https://reqres.in")
        .inferHtlmResources(BlackList(""".*\.js""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.css"""))

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
        .exec(http("GET_USERS_01"))
            .get("/api/users?page=2")
            .headers(header)
        .pause(3)

        .exec(http("GET_USER_01"))
            .get("/api/users/2")
            .headers(header)
        .pause(2)

        .exec(http("POST_USER_01"))
            .post("/api/users")
            .body(RawFileBody("""{"name":"morpheus","job":"leader"}"""))
            .check(status.is(201))

        setUp(scn
            .inject(
                atOnceUsers(10),
                nothingFor(5 seconds),
                rampUsers(10) over (5 seconds)
            ))
            .protocols(httpProtocol)
}
