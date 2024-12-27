package day13

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day13/input.txt")
    val rows = parseLines(input)

    val costs = ClawMachine(rows).calculateCostsForSolvableMachines()
    println("Lowest price sum for all machines: $costs")

}