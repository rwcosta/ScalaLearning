import math.pow
import io.StdIn.readDouble

object Pattern {
    def main(args: Array[String]) {
        print("Digite x: ")
        var x: Double = readDouble()

        println("\n1 - Valor: " + eq1(x))
        println("2 - Valor: " + eq2(x))
        println("3 - Valor: " + eq3(x))
    }

    def eq1(x: Double): Double = x match {
        case x if(x >= 2 ) => x - 2
        case x if(x >= -2 && x < 2) => pow(x, 2) - 4
        case x if (x < -2) => 3
    }

    def eq2(x: Double): Double = x match {
        case x if(x < 4) => x+2
        case 4 => 3
        case x if(x > 4) => 6 - x 
    }

    def eq3(x: Double): Double = x match {
        case x if(x <= -1) => x + 2
        case _ => pow(x, 2)
    }
}

/*
Pattern match for:
Eq1:
    x - 2, se x >= 2
    x² - 4, se -2 <= x < 2
    3, se x < -2

Eq2:
    x + 2, se x < 4
    3, se x = 4
    6 - x, se x > 4

Eq3:
    x+2, se x <= -1
    x², se x > -1
*/
