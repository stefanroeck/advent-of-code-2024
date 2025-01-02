package day16

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day16/input.txt")
    val rows = parseLines(input)

    val cost = ReindeerMaze(rows).shortestPathCost()
    println("Shortest path cost: $cost")
}