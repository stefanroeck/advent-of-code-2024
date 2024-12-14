package day03

import util.FileUtil

fun main() {
    val input = FileUtil.readFile("/day03/input.txt")

    val sum = Multiplier.multiply(input)
    println("sum of all products: $sum")

    val sumWithInstructions = Multiplier.multiplyWithDoAndDont(input)
    println("sum of all products with enhanced instructions: $sumWithInstructions")

}