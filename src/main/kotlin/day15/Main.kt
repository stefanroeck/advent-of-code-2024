package day15

import day15.MapType.NORMAL
import day15.MapType.WIDE
import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day15/input.txt")
    val rows = parseLines(input)

    val gpsCoordinateSumNormal = Warehouse(rows, mapType = NORMAL).walkAroundAndReturnSumOfGpsCoordinate()
    println("Sum of normal box's GPS coordinates: $gpsCoordinateSumNormal")

    val gpsCoordinateSumWide = Warehouse(rows, mapType = WIDE).walkAroundAndReturnSumOfGpsCoordinate()
    println("Sum of wide box's GPS coordinates: $gpsCoordinateSumWide")

}