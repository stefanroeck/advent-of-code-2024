package day04

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day04/input.txt")
    val rows = parseLines(input)

    val sum = WordSearch().count(rows, "XMAS")
    println("sum of all XMAS words: $sum")

    val xmasPatterns = WordSearch().countXmasPattern(rows)
    println("sum of all X-MAS patterns: $xmasPatterns")

}