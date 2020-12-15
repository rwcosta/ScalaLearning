# **Modularização dos Testes**

Para uma melhor organização modularizamos o teste utilizado em [simulation](https://github.com/rwcosta/ScalaLearning/blob/main/Gatling%20Testes/simulation.md) com algumas modificações. O projeto foi dividido em 4 partes:

  - [**Config**](#config)
  - [**Requests**](#requests)
  - [**Scenarios**](#scenarios)
  - [**Simulations**](#simulations)
  - [**Código-fonte**](https://github.com/rwcosta/ScalaLearning/tree/main/Gatling%20Testes/Gatling%20Maven/gatling-framework/src/test/scala/simulations/reqres)

## **Config**

Será criado um objedo **Config** que conterá a nossa url base, parâmetros e valores default dos parâmetros:

```Scala
package config

object ConfigReqres {
    private def getProperty(name: String, defaultValue: String): String = {
        Option(System.getenv(name))
            .orElse(Option(System.getProperty(name)))
            .getOrElse(defaultValue)
    }

    val header = Map(
        "content-type" -> "application/json"
    )

    val baseUrl = "https://reqres.in"

    def users: Int = getProperty("users", "1").toInt
}
```

## **Requests**

Aqui conterão os requests separados em seus respectivos objetos, no nosso caso foram definidos 4 requests:

### **GetUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object GetUser {
    val getUser = exec(http("Get user")
        .get(baseUrl + "/api/users/${userId}")
        .headers(header)
        .check(status.is(200))
    )
}
```

### **GetUsers:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object GetUsers {
    val getUsers = exec(http("Get users")
        .get(baseUrl + "/api/users")
        .queryParam("page", 2)
        .check(status.is(200))
        .check(jsonPath("$.data[1].id").saveAs("userId"))
    )
}
```

### **PostUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object PostUser {
    val postUser = exec(http("Post user")
        .post(baseUrl + "/api/users")
        .body(StringBody("""{ "name": "James", "job": "spy" }""")).asJson
        .check(status.is(201))
        .check(jsonPath("$.id").saveAs("userId"))
    )
}
```

### **PutUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object PutUser {
    val putUser = exec(http("Put user")
        .put(baseUrl + "/api/users/${userId}")
        .body(StringBody("""{ "name": "Bond", "job": "secret spy" }""")).asJson
        .headers(header)
        .check(status.is(200))
        .check(regex("updatedAt").find.exists)
    )
}
```

### **DeleteUser:**

```Scala
package requests

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import config.ConfigReqres.{baseUrl, header}

object DeleteUser {
    val deleteUser = exec(http("Delete user")
        .delete(baseUrl + "/api/users/${userId}")
        .check(status.is(204))
    )
}
```

## **Scenarios**

Aqui conterão os scenarios que poderam ser setados em nossas simulações, no nosso caso foram definidos 3 scenarios:

### **PostDeleteScenario:**

```Scala
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
```

### **GetDeleteScenario:**

```Scala
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
```

## **Simulations**

Aqui conterão as nossas simulações, no nosso caso apenas uma simulação foi definida:

```Scala
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
        details("Post user").responseTime.max.lte(600),
        details("Put user").responseTime.max.lte(500),
        details("Delete user").responseTime.max.lte(600),
        global.failedRequests.percent.lte(5)
    )
}
```
### **Assertions**

Os assertions de escopo `details(group)` foram utilizados para definir um tempo de resposta máximo em ms de cada tipo de request. O nome do grupo de requests é definido no `http(g: String)` que precede os `exec()` da sequência de requests.  
