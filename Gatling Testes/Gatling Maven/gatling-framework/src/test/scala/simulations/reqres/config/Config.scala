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
