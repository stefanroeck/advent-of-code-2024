package day11

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day11/input.txt")
    val rows = parseLines(input)

    val stones = StonesOnPluto.blink(rows.first(), 25).split(" ").size
    println("Number of stones after blinking: $stones")

}