object EvenOdd {
    def main(args: Array[String]) {
        print("Digite o valor: ")
        var n = scala.io.StdIn.readInt()

        var s = if(n%2 == 0) "Par" else "Impar"

        println(s)
    }
}