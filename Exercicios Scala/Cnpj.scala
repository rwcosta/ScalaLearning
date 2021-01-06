import scala.collection.mutable.ArrayBuffer

object Cnpj {
    def main(args: Array[String]) = {
        println(cnpj())
    }

    def cnpj(): String = {
        var v = scala.collection.mutable.ArrayBuffer[Int]()

        for (i <- 1 to 12) {
            v += scala.util.Random.nextInt(10)
        }

        var d1 = Vector(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        var d2 = Vector(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        /* Primeiro digito verificador */
        v += digit(v, d1)

        /* Segundo digito verificador */
        v += digit(v, d2)

        return toString(v)
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