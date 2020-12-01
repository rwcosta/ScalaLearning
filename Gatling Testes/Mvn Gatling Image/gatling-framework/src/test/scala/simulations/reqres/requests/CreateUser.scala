package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object CreateUser {
    val createUser = exec(http("Create new user")
        .post(myUrl + "/api/users")
        .body(StringBody("""{ "name": "morpheus", "job": "leader" }""")).asJson
        .check(status.is(201))
    )
}
