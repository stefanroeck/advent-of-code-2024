package day07

import util.FileUtil
import util.InputUtils

fun main() {
    val input = FileUtil.readFile("/day07/input.txt")
    val sum = BridgeRepair.findOperatorsAndCalcSum(InputUtils.parseLines(input))

    println("sum of all correct bridge operations: $sum")
}