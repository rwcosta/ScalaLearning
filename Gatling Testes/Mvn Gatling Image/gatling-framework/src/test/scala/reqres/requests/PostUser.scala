package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object PostUser {
    val postUser = exec(http("Post user")
        .post(baseUrl + "/api/users")
        .body(StringBody("""{ "name": "James", "job": "spy" }""")).asJson
        .check(status.is(201))
        .check(jsonPath("$.id").saveAs("userId"))
        .check(bodyString.saveAs("postResponse"))
    )
}
