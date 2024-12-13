package day02

import kotlin.math.abs

object Reports {

    data class Report(val levels: List<Int>, private val withDampener: Boolean = false) {

        private val variantsWithSingleLevelSkipped = if (withDampener) variantsWithSingleLevelSkipped() else emptyList()

        fun isIncreasing(): Boolean {
            return levels == levels.sorted()
        }

        fun isDecreasing(): Boolean {
            return levels == levels.sortedDescending()
        }

        fun isStable(): Boolean {
            val distances = levels
                .mapIndexed { idx, level -> if (idx > 0) abs(levels[idx - 1] - level) else -1 }
                .subList(1, levels.size)
            return distances.all { it in 1..3 }
        }

        fun isSafe(): Boolean {
            return ((isIncreasing() || isDecreasing()) && isStable())
                    || withDampener && variantsWithSingleLevelSkipped.any { it.isSafe() }
        }

        internal fun variantsWithSingleLevelSkipped(): List<Report> {
            return IntRange(0, levels.size - 1)
                .map { levels.filterIndexed { idx, _ -> it != idx } }
                .map { Report(levels = it, withDampener = false) }
        }

    }

    fun extractReports(input: String): List<Report> = input.split("\n")
        .map { it.trim() }
        .filterNot { it.isEmpty() }
        .map { line -> Report(line.split(" ").map { it.toInt() }) }
        .toList()

    fun countSafeReports(input: String): Int {
        val reports = extractReports(input)

        return reports.count { it.isSafe() }
    }

    fun countSafeReportsWithDampener(input: String): Int {
        val reports = extractReports(input).map { it.copy(withDampener = true) }

        return reports.count { it.isSafe() }
    }
}