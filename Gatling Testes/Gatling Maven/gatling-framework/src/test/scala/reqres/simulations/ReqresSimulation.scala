package simulations

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres._
import scenarios.{PostDeleteScenario, GetDeleteScenario}

class ReqresSimulation extends Simulation {
    val postDeleteExec = PostDeleteScenario.postDeleteScenario
        .inject(
            atOnceUsers(users),
            nothingFor(3 seconds),
            heavisideUsers(users) during(10 seconds),
        )

    val getDeleteExec = GetDeleteScenario.getDeleteScenario
        .inject(
            constantUsersPerSec(users) during(5 seconds),
            nothingFor(5 seconds),
            rampUsers(users) during(5 seconds)
        )

    setUp(
        postDeleteExec,
        getDeleteExec
    )
    .assertions(
        details("Get users").responseTime.max.lte(250),
        details("Get user").responseTime.max.lte(500),
        details("Post user").responseTime.max.lte(650),
        details("Put user").responseTime.max.lte(500),
        details("Delete user").responseTime.max.lte(600),
        global.failedRequests.percent.lte(5)
    )
}
