package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object UpdateUser {
    val updateUser = exec(http("Update user")
        .put(myUrl + "/api/users/2")
        .body(StringBody("""{ "name": "morpheus", "job": "zion resident" }""")).asJson
        .check(status.is(200))
    )
}
