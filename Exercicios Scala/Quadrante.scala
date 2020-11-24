object Quadrante {
    def main(args: Array[String]) {
        print("Digite x: ")
        var x: Double = scala.io.StdIn.readDouble()

        print("Digite y: ")
        var y: Double = scala.io.StdIn.readDouble()

        var s: String = if(x > 0 && y > 0) "O ponto está no primeiro quadrante"
                        else if(x < 0 && y > 0) "O ponto está no segundo quadrante"
                        else if(x < 0 && y < 0) "O ponto está no terceiro quadrante"
                        else "O ponto está no quarto quadrante"

        println(s)
    }
}

/*
Quadrante de uma coordenada
*/
