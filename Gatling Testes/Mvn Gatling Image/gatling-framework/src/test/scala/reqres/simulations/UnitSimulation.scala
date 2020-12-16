package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres._
import scenarios.UnitScenario

class UnitSimulation extends Simulation {
    setUp(
        UnitScenario.unitScenario
            .inject(
                atOnceUsers(1)
            )
    )
}