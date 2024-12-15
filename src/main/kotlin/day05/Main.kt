package day05

import util.FileUtil

fun main() {
    val input = FileUtil.readFile("/day05/input.txt")

    val sum = PageUpdates.parseInput(input).sumOfMiddlePageNumbersForCorrectUpdates()
    println("sum of all correct update's middle page numbers: $sum")
}