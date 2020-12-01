package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.UpdateUser

object UpdateUserScenario {
    val updateUserScenario = scenario("Update user scenario")
        .exec(UpdateUser.updateUser).pause(2 seconds)
}
