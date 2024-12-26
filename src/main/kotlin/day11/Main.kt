package day11

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day11/input.txt")
    val rows = parseLines(input)

    val stonesAfter25Blinks = StonesOnPluto.blink(rows.first(), 25).split(" ").size
    println("Number of stones after blinking: $stonesAfter25Blinks")

    val stonesAfter75Blinks = StonesOnPluto.countBlinks(rows.first(), 75)
    println("Number of stones after blinking: $stonesAfter75Blinks")
}