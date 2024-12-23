package day09

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day09/input.txt")
    val rows = parseLines(input)

    check(rows.size == 1) { "Single row expected" }

    val checksum = DiskFragmenter.createMapDefragAndReturnChecksum(rows.first())
    println("Checksum after defrag: $checksum")
}