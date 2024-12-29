package day13

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day13/input.txt")
    val rows = parseLines(input)

    val costs = ClawMachine(rows).calculateCostsForSolvableMachines()
    println("Lowest price sum for all machines: $costs")

    // too high: 94.467.057.936.736
    val costsWithCorrectCoordinates = ClawMachine(rows).calculateCostsForSolvableMachinesWithCorrectedPriceCoordinates()
    println("Lowest price sum for all fixed coordinates machines: $costsWithCorrectCoordinates")
}