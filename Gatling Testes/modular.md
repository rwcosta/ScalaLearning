# **Modularização dos Testes**

Para uma melhor organização modularizamos o teste utilizado em [simulation](https://github.com/rwcosta/ScalaLearning/blob/main/Gatling%20Testes/simulation.md). O projeto foi dividido em 4 partes:

  - [**Config**](#config)
  - [**Requests**](#requests)
  - [**Scenarios**](#scenarios)
  - [**Simulations**](#simulations)

## **Config**

Será criado um objedo **Config** que conterá a nossa url base, parâmetros e valores default dos parâmetros:

```Scala
package config

object Config {
    private def getProperty(name: String, defaultValue: String): String = {
        Option(System.getenv(name))
            .orElse(Option(System.getProperty(name)))
            .getOrElse(defaultValue)
    }

    val myUrl = "https://reqres.in"

    def users: Int = getProperty("users", "1").toInt
    def rampDuration: Int = getProperty("ramp_duration", "10").toInt
    def rampPerSecRate: Int = getProperty("ramp_per_sec_rate", s"${users+10}").toInt
    def constantUserDuration: Int = getProperty("const_user_duration", "10").toInt
    def heavisideMult: Int = getProperty("heaviside_mult", "2").toInt
    def heavisideDuration: Int = getProperty("heaviside_duration", "10").toInt
}
```

## **Requests**

Aqui conterão os requests separados em seus respectivos objetos, no nosso caso foram definidos 4 requests:

### **GetUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object GetUser {
    val header = Map("content-type" -> "application/json")

    val getUser = exec(http("Get user")
        .get(myUrl + "/api/users/2")
        .headers(header)
    )
}
```

### **GetUsers:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object GetUsers {
    val header = Map("content-type" -> "application/json")

    val getUsers = exec(http("Get users")
        .get(myUrl + "/api/users?page=2")
        .headers(header)
    )
}
```

### **CreateUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object CreateUser {
    val createUser = exec(http("Create new user")
        .post(myUrl + "/api/users")
        .body(StringBody("""{ "name": "morpheus", "job": "leader" }""")).asJson
        .check(status.is(201))
    )
}
```

### **UpdateUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.Config.myUrl

object UpdateUser {
    val updateUser = exec(http("Update user")
        .put(myUrl + "/api/users/2")
        .body(StringBody("""{ "name": "morpheus", "job": "zion resident" }""")).asJson
        .check(status.is(200))
    )
}
```

## **Scenarios**

Aqui conterão os scenarios que poderam ser setados em nossas simulações, no nosso caso foram definidos 3 scenarios:

### **CreateUserScenario:**

```Scala
package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.CreateUser

object CreateUserScenario {
    val createUserScenario = scenario("Create user scenario")
        .exec(CreateUser.createUser).pause(2 seconds)
}
```

### **GetUsersScenario:**

```Scala
package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.{GetUser, GetUsers}

object GetUsersScenario {
    val getUsersScenario = scenario("Get users scenario")
        .exec(GetUser.getUser).pause(2 seconds)
        .exec(GetUsers.getUsers).pause(2 seconds)
}
```

### **UpdateUserScenario:**

```Scala
package scenarios

import scala.concurrent.duration._
import io.gatling.core.Predef.scenario
import requests.UpdateUser

object UpdateUserScenario {
    val updateUserScenario = scenario("Update user scenario")
        .exec(UpdateUser.updateUser).pause(2 seconds)
}
```

## **Simulations**

Aqui conterão as nossas simulações, no nosso caso apenas uma simulação foi definida:

```Scala
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
```
### **Assertions**

Os assertions de escopo `details(group)` foram utilizados para definir um tempo de resposta máximo em ms de cada tipo de request. O nome do grupo de requests é definido no `http(g: String)` que precede os `exec()` da sequência de requests.  
