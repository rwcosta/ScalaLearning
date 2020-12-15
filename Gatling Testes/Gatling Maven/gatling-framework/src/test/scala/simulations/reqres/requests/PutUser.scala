package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object PutUser {
    val putUser = exec(http("Put user")
        .put(baseUrl + "/api/users/${userId}")
        .body(StringBody("""{ "name": "Bond", "job": "secret spy" }""")).asJson
        .headers(header)
        .check(status.is(200))
        .check(regex("updatedAt").find.exists)
    )
}
