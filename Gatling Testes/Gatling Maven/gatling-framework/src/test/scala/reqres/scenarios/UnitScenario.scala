package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef._
import config.ConfigReqres.writeReponse
import requests.{GetUsers, PostUser, GetUser, PutUser, DeleteUser}

object UnitScenario {
    val unitScenario = scenario("Post scenario")
        .exec(PostUser.postUser)
        .pause(2 seconds)

        .exec { session =>
            writeReponse(session("postResponse").as[String], "src/test/resources/UnitScenario/post_response.json")
            session
        }

        .exec(PutUser.putUser)
        .pause(2 seconds)

        .exec { session =>
            writeReponse(session("putResponse").as[String], "src/test/resources/UnitScenario/put_response.json")
            session
        }

        .exec(GetUsers.getUsers)
        .pause(2 seconds)

        .exec { session =>
            writeReponse(session("getsResponse").as[String], "src/test/resources/UnitScenario/gets_response.json")
            session
        }

        .exec(GetUser.getUser)
        .pause(2 seconds)

        .exec { session =>
            writeReponse(session("getResponse").as[String], "src/test/resources/UnitScenario/get_response.json")
            session
        }
}
