package day06

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day06/input.txt")
    val labMap = LabWithGuard.buildLabMap(parseLines(input))

    val updatedMap = LabWithGuard.sendGuardOnPatrol(labMap)
    println("number of visited guard locations: ${updatedMap.visitedLocations().size}")

    val loopTrapObstructions = LabWithGuard.findLoopTrapObstructions(labMap)
    println("number of loop trap obstructions: ${loopTrapObstructions.size}")
}