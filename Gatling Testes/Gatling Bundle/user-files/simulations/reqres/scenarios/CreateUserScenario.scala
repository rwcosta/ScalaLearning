package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.CreateUser

object CreateUserScenario {
    val createUserScenario = scenario("Create user scenario")
        .exec(CreateUser.createUser).pause(2 seconds)
}
