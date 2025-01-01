package day15

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day15/input.txt")
    val rows = parseLines(input)

    val warehouse = Warehouse(rows)

    val gpsCoordinateSum = warehouse.walkAroundAndReturnSumOfGpsCoordinate()
    println("Sum of box's GPS coordinates: $gpsCoordinateSum")
}