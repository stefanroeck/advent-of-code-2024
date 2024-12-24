package day10

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day10/input.txt")
    val rows = parseLines(input)

    val score = HikingTrails(rows).score()
    println("Hiking Trails score: $score")
    
}