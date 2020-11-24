object StoI {
    def main(args: Array[String]) {
        print("Digite a string: ")
        var s: String = scala.io.StdIn.readLine()

        convStr(s) match {
            case Some(s) => println("Valor Int: " + s*2)
            case None => println("Valor digitado eh invalido") 
        }
    }

    def convStr(s: String): Option[Int] = try {
        Some(s.toInt)
    }
    catch {
        case e: NumberFormatException => None
    }
}

/*
Tratamento de erro no toInt
*/
