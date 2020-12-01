package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object GetUser {
    val header = Map("content-type" -> "application/json")

    val getUser = exec(http("Get user")
        .get(myUrl + "/api/users/2")
        .headers(header)
    )
}
