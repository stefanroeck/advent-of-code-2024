package day12

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day12/input.txt")
    val rows = parseLines(input)

    val price = GardenPlots(rows).calculatePriceWithPerimeter()
    println("Price for all fences: $price")

    val discountedPrice = GardenPlots(rows).calculatePriceWithSides()
    println("Discounted price for all fences: $discountedPrice")
}