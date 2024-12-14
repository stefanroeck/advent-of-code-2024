package day04

fun main() {
    val file = "/day04/input.txt"
    val input = object {}::class.java.getResource(file)?.readText()
    checkNotNull(input) { "cannot read input from $file" }

    val sum = WordSearch.count(input, "XMAS")
    println("sum of all XMAS words: $sum")
}