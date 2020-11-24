object Lower {
    def main(args: Array[String]) {
        print("Digite a: ")
        var a: Int = scala.io.StdIn.readInt()

        print("Digite b: ")
        var b: Int = scala.io.StdIn.readInt()

        println("\nMenor: " + lower(a, b))
    }

    def lower(a: Int, b: Int): Int = {
        return if(a <= b) a else b
    }
}

/*
Função menor
*/
