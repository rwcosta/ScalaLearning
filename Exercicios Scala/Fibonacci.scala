object Fibonacci {
    def main(args: Array[String]) {
        print("Digite o valor: ")
        var n: Int = scala.io.StdIn.readInt() 

        println(fib(n))
    }

    def fib(n: Int): Int = {
        if(n == 0)
            return 0
        if(n <= 2)
            return 1

        return fib(n-1) + fib(n-2)
    }
}

/*
Elemento da sequencia fibonacci
*/
