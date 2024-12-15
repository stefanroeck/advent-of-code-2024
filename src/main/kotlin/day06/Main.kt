package day06

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day06/input.txt")
    val labMap = LabWithGuard.buildLabMap(parseLines(input))

    val updatedMap = labMap.sendGuardOnPatrol()
    val visitedLocations = updatedMap.visitedLocations()
    println("number of visited guard locations: $visitedLocations")
}