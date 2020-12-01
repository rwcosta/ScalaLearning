package simulations

import scala.concurrent.duration._
import io.gatling.core.Predef._
import scenarios._
import config.Config._

class ReqresSimulation extends Simulation {
    val createUserExec = CreateUserScenario.createUserScenario
        .inject(
            atOnceUsers(users),
            nothingFor(5 seconds),
            constantUsersPerSec(users) during(constantUserDuration)
        )

    val getUserExec = GetUsersScenario.getUsersScenario
        .inject(
            heavisideUsers(users*heavisideMult) during(heavisideDuration),
            nothingFor(5 seconds),
            rampUsersPerSec(users) to rampPerSecRate during(rampDuration)
        )

    val updateUserExec = UpdateUserScenario.updateUserScenario
        .inject(
            atOnceUsers(users),
            nothingFor(3 seconds),
            constantUsersPerSec(users) during(constantUserDuration)
        )

    setUp(
        createUserExec,
        getUserExec,
        updateUserExec    
    )            
    .assertions(
        details("Create new user").responseTime.max.lte(620),
        details("Get users").responseTime.max.lte(200),
        details("Get user").responseTime.max.lte(170),
        details("Update user").responseTime.max.lte(800),
        global.failedRequests.percent.lte(5)
    )
}
