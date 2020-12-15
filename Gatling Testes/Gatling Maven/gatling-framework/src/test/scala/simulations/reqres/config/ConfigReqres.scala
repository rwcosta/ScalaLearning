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
