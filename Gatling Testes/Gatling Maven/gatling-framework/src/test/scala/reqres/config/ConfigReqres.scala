package config

import java.io.{File, PrintWriter, IOException, FileNotFoundException}

object ConfigReqres {
    private def getProperty(name: String, defaultValue: String): String = {
        Option(System.getenv(name))
            .orElse(Option(System.getProperty(name)))
            .getOrElse(defaultValue)
    }

    def writeReponse(dataBytes: String, path: String) {
        try {
            val file = new File(path)

            if(!file.exists())
                file.createNewFile()

            val pw = new PrintWriter(file)
            pw.write(dataBytes)

            pw.close()
        } catch {
            case e: FileNotFoundException => println("Couldn't find that file.")
            case e: IOException => println("Had an IOException trying to read that file")
        }
    }

    val header = Map(
        "Content-Type" -> "application/json"
    )

    val baseUrl = "https://reqres.in"

    def users: Int = getProperty("users", "1").toInt
}
