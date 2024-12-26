package day12

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day12/input.txt")
    val rows = parseLines(input)

    val price = GardenPlots(rows).calculatePrice()
    println("Price for all fences: $price")
}