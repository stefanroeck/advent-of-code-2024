package day04

import util.FileUtil

fun main() {
    val input = FileUtil.readFile("/day04/input.txt")

    val sum = WordSearch.count(input, "XMAS")
    println("sum of all XMAS words: $sum")
}