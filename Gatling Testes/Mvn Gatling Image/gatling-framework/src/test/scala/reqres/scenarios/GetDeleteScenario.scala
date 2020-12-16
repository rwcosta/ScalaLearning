package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.{GetUsers, GetUser, DeleteUser}

object GetDeleteScenario {
    val getDeleteScenario = scenario("GetDelete scenario")
        .exec(GetUsers.getUsers)
        .pause(2 seconds)

        .exec(GetUser.getUser)
        .pause(2 seconds)

        .exec(DeleteUser.deleteUser)
        .pause(2 seconds)
}
