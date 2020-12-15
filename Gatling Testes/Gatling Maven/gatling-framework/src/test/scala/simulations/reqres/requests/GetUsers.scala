package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object GetUsers {
    val getUsers = exec(http("Get users")
        .get(baseUrl + "/api/users")
        .queryParam("page", 2)
        .check(status.is(200))
        .check(jsonPath("$.data[1].id").saveAs("userId"))
    )
}
