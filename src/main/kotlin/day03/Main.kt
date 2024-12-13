package day03

fun main() {
    val file = "/day03/input.txt"
    val input = object {}::class.java.getResource(file)?.readText()
    checkNotNull(input) { "cannot read input from $file" }

    val sum = Multiplier.multiply(input)

    println("sum of all products: $sum")
}