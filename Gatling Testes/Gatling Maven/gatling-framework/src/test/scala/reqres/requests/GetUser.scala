package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object GetUser {
    val getUser = exec(http("Get user")
        .get(baseUrl + "/api/users/${userId}")
        .headers(header)
        .check(status.is(200))
        .check(bodyString.saveAs("getResponse"))
    )
}
