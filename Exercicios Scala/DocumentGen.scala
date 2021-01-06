import scala.collection.mutable.ArrayBuffer

object DocumentGen {
    def main(args: Array[String]) = {
        println("CNPJ - " + cnpj())
        println("CPF  - " + cpf())
    }

    def cnpj(): String = {
        /* Valores aleatorios */
        var v = randArray(12)

        /* Primeiro digito verificador */
        v += digit(v, Vector(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2))

        /* Segundo digito verificador */
        v += digit(v, Vector(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2))

        return toString(v)
    }

    def cpf(): String = {
        /* Valores aleatorios */
        var v = randArray(9)

        /* Primeiro digito verificador */
        v += digit(v, Vector(10, 9, 8, 7, 6, 5, 4, 3, 2))

        /* Segundo digito verificador */
        v += digit(v, Vector(11, 10, 9, 8, 7, 6, 5, 4, 3, 2))

        return toString(v)
    }

    /* Retorna um ArrayBuffer de r elementos aleatórios */
    def randArray(r: Int): ArrayBuffer[Int] = {
        var v = ArrayBuffer[Int]()

        for (i <- 1 to r) {
            v += scala.util.Random.nextInt(10)
        }

        return v
    }

    /* Retorna o digito verificador resultante do produto entre o ArrayBuffer e o Vector */
    def digit(v: ArrayBuffer[Int], d: Vector[Int]): Int = {
        var sum: Int = 0

        for(i <- 0 to d.length-1) {
            sum += v(i) * d(i)
        }

        sum %= 11

        if(sum < 2) return 0
        else return 11 - sum
    }

    /* Retorna ArrayBuffer em String */
    def toString(v: ArrayBuffer[Int]): String = {
        var s: String = ""

        for(i <- 0 to v.length-1) {
            s += v(i)
        }

        return s
    }
}