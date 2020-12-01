package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.{GetUser, GetUsers}

object GetUsersScenario {
    val getUsersScenario = scenario("Get users scenario")
        .exec(GetUser.getUser).pause(2 seconds)
        .exec(GetUsers.getUsers).pause(2 seconds)
}
