package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object GetUsers {
    val header = Map("content-type" -> "application/json")

    val getUsers = exec(http("Get users")
        .get(myUrl + "/api/users?page=2")
        .headers(header)
    )
}
