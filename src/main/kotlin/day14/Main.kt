package day14

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day14/input.txt")
    val rows = parseLines(input)

    val bathRoomSecurity = BathRoomSecurity(rows, width = 101, height = 103)

    val safetyFactor = bathRoomSecurity.predictMovement(1000)
    println("Safety factor in front of the bath room: $safetyFactor")

    bathRoomSecurity.findChristmasTree(startAtSteps = 7583, stopAtSteps = 7583)

}