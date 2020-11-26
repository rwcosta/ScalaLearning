# Simulações em Gatling

## **Imports**

No Gatling, para se utilizar da DSL é necessário fazer alguns imports:

```Scala
import scala.concurrent.duration._ /* para especificar a grandeza na duração de tempo */

import io.gatling.core.Predef_ /* core para a DSL */
import io.gatling.http.Predef_ /* protocolo http */
import io.gatling.jdbc.Predef_ /* caso utilize feeder */
```

## **Estrutura**

A classe de teste precisa estender a classe Simulation que é estruturada em 4 partes:

### **HTTP Protocol:**

O Gatling HTTP faz com que seja possível utilizar o protocolo HTTP para realizar os testes que podem ser aplicados em APIs, sites e web services. Utilizando `http.baseUrl()` setamos o **httpProtocol** para o nosso scenario. Vale ressaltar que também é possível realizar o teste em vários alvos ao mesmo tempo utilizando o `http.baseUrls()`.

No nosso caso, o teste foi aplicado na API [Reqres](https://reqres.in/):

```Scala
val httpProtocol = http.baseUrl("https://reqres.in")
```

O protocolo também nos permite várias outras configurações desde whitelists, blacklists e headers.

### **Headers:**

Pode-se definir um header específico para cada request. Os headers em gatling suportam as configurações dos headers no protocolo HTTP, como o accept-language, referer, Do Not Track, content-type, etc.

O nosso teste é de cunho didático então não é preciso um header muito complexo:

```Scala
val header = Map(
    "content-type" -> "application/json"
)
```

### **Scenario:**

Aqui é onde vai ser definido um cenário de teste que contém os requests que são enviados através do método `.exec(http(requestName: String).request)` e que geralmente é seguido por um `.pause()` que pode simular o tempo que um usuário levaria para realizar outro request ou também pode ser utilizado para organização e legibilidade do teste. Definimos um scenario **MyFirstTest**:

```Scala
val scn = scenario("MyFirstTest")
```

No exec está contido o nome do request e o próprio request que, por sua vez, contém o método e a URL. O próprio gatling provém os métodos mais comuns como os utilizados em REST: get, put, post, delete, patch.

#### **Get:**

```Scala
.exec(http("GET_USERS_01")
    .get("/api/users?page=2")
    .headers(header))
.pause(2)
```
#### **Post:**

O gatling também dá suporte à passagem de parâmetros para os métodos que necessitam de um, como o post. Uma forma de fazer isso é usando o `.formParam(“key”, “value”)`:

```Scala
.exec(http("POST_VALUE")
    .post("my.form-action.uri")
    .formParam("myKey", "myValue"))
.pause(2)
```

Também é possível adicionar um corpo ao request utilizando o `.body()` e assim passar um arquivo ou objeto JSON por parâmetro:

```Scala
.exec(http("POST_USER_01")
    .post("/api/users")
    .body(StringBody("""{ "name": "morpheus", "job": "leader" }""")).asJson
    .check(status.is(201)))
.pause(2)
```

Utilizando o **StringBody** passa-se uma string por parâmetro mas também é possível passar diretamente um arquivo utilizando o **RawFileBody**, no caso do JSON `.body(RawFileBody(“object.json”)).asJson`. No caso do bundle baixado no gatling.io, o arquivo .json precisa estar dentro da pasta `user-files/resources`.

#### **Put:**

```Scala
.exec(http("PUT_USER_01")
    .put("/api/users/2")
    .body(StringBody(s""" {"name": "morpheus", "job": "zion resident", "updatedAt": "${java.time.LocalDate.now}" } """))
    .check(status.is(200)))
.pause(2)
```

### **Simulation**

Aqui é onde a simulação é definida, é informada a quantidade de usuários a serem injetados no scenario e a frequência com que estes usuários são injetados. Para isso, o gatling provém alguns métodos para auxiliar na tarefa:

* **`atOnceUsers(nUsers)`**: Inejta nUsers de uma única vez.

* **`nothingFor(duration)`**: Não faz nada durante **duration**.

* **`constantUsersPerSec(rate) during(duration)`**: Injeta **rate** usuários por segundo durante **duration**.

* **`constantUsersPerSec(rate) during(duration) radomized`**: Injeta **rate** usuários durante **duration**, mas a injeção é feita em intervalos aleatórios.

* **`rampUsers(nUsers) over(duration)`**: Injeta **nUsers** de maneira constante e distribuida dentro de **duration**.

* **`rampUsersPerSec(rate1) to (rate2) during(duration)`**: Adiciona de **rate1** à **rate2** usuários por segundo de forma não-constante, mantendo a rampa de usuários, durante **duration**.

* **`rampUsersPerSec(rate1) to (rate2) during(duration) randomized`**: Adiciona de **rate1** à **rate2** usuários de forma não-constante e mantendo a rampa de usuários. A injeção de usuários ocorre em intervalos aleatórios e dura **duration**.

* **`heavisideUsers(nUsers) during(duration)`**: Injeta **nUsers** de forma a simular melhor um pico real de usuários.

Setamos uma simulação simples:

```Scala
setUp(scn
    .inject(
        atOnceUsers(10),
        nothingFor(3 seconds),
        heavisideUsers(50) during(10 seconds),
        nothingFor(5 seconds),
        constantUsersPerSec(10) during(5 seconds),
        nothingFor(5 seconds),
        rampUsersPerSec(10) to 15 during(5 seconds)
    ))
    .protocols(httpProtocol)
```

## **Referências**

* [Documentação Gatling](https://gatling.io/docs/current/)
* [Gatling Tutorials for Beginners by James Willet](https://www.youtube.com/watch?v=6Uc--YQMwf4&list=PLw_jGKXm9lIYpTotIJ-R31pXS7qqwXstt)
* [Testes de carga e performance com Gatling.io – Eduardo Costa](https://www.youtube.com/watch?v=-tk24HMG41g)
* [Gatling Load Testing Part 1 – Using Gatling](https://blog.codecentric.de/en/2017/06/gatling-load-testing-part-1-using-gatling/)
