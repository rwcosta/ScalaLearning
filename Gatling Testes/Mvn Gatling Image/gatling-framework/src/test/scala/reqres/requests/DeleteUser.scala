package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object DeleteUser {
    val deleteUser = exec(http("Delete user")
        .delete(baseUrl + "/api/users/${userId}")
        .check(status.is(204))
    )
}
