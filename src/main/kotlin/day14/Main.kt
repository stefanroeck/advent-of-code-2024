package day14

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day14/input.txt")
    val rows = parseLines(input)

    val safetyFactor = BathRoomSecurity(rows, width = 101, height = 103).predictMovement(100)
    println("Safety factor in front of the bath room: $safetyFactor")

}