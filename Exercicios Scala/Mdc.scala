object Mdc {
    def main(args: Array[String]) {
        print("Digite a: ")
        var a: Int = scala.io.StdIn.readInt()

        print("Digite b: ")
        var b: Int = scala.io.StdIn.readInt()

        println("Mdc: " + mdc(a, b))
    }

    def lower(a: Int, b: Int): Int = {
        return if(a <= b) a else b
    }

    def mdc(a: Int, b: Int): Int = {
        for(l <- lower(a, b) to 2 by -1) {
            if(a % l == 0 && b % l == 0)
                return l
        }

        return 1
    }
}


/*
Cálculo MDC
*/
