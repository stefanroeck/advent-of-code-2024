package day05

import util.FileUtil

fun main() {
    val input = FileUtil.readFile("/day05/input.txt")
    val instructions = PageUpdates.parseInput(input)

    val sumCorrectUpdates = instructions.sumOfMiddlePageNumbersForCorrectUpdates()
    println("sum of all correct update's middle page numbers: $sumCorrectUpdates")

    val sumIncorrectUpdates = instructions.sumOfMiddlePageNumbersForIncorrectUpdates()
    println("sum of all incorrect update's middle page numbers: $sumIncorrectUpdates")
}