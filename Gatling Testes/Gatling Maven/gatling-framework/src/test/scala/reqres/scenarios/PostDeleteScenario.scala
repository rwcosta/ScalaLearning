package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.{PostUser, GetUser, PutUser, DeleteUser}

object PostDeleteScenario {
    val postDeleteScenario = scenario("PostDelete scenario")
        .exec(PostUser.postUser)
        .pause(2 seconds)

        .exec(PutUser.putUser)
        .pause(2 seconds)

        .exec(DeleteUser.deleteUser)
        .pause(2 seconds)
}
