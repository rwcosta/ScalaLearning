object Upper {
    def main(args: Array[String]) {
        print("Digite a: ")
        var a: Int = scala.io.StdIn.readInt()

        print("Digite b: ")
        var b: Int = scala.io.StdIn.readInt()

        var s: String = if(a > b) s"$a maior que $b" else s"$a menor que $b"

        println(s)
    }
}

/*
Função maior
*/
