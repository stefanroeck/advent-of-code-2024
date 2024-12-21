package day08

import util.FileUtil
import util.InputUtils.parseLines

fun main() {
    val input = FileUtil.readFile("/day08/input.txt")
    val rows = parseLines(input)

    val antinodeCount = AntennaAntinodes(rows).countAntinodes()
    println("number of antinodes: $antinodeCount")

    val resonantHarmonicAntinodeCount = AntennaAntinodes(rows).countAntinodesWithResonantHarmonics()
    println("number of resonant harmonic antinodes: $resonantHarmonicAntinodeCount")
}