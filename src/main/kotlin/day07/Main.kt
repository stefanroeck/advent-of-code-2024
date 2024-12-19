package day07

import util.FileUtil
import util.InputUtils

fun main() {
    val input = FileUtil.readFile("/day07/input.txt")
    val lines = InputUtils.parseLines(input)

    val sumTwoOperators = BridgeRepair.findTwoOperatorsAndCalcSum(lines)
    println("sum of all correct bridge operations with 2 operators: $sumTwoOperators")

    val sumThreeOperators = BridgeRepair.findThreeOperatorsAndCalcSum(lines)
    println("sum of all correct bridge operations with 3 operators: $sumThreeOperators")

}