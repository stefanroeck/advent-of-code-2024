package day02

object Reports {

    data class Report(val levels: List<Int>) {
        fun isIncreasing(): Boolean {
            return levels.equals(levels.sorted())
        }

        fun isDecreasing(): Boolean {
            return levels.equals(levels.sortedDescending())
        }

        fun isStable(): Boolean {
            val distances = levels
                .mapIndexed { idx, level -> if (idx > 0) Math.abs(levels[idx - 1] - level) else -1 }
                .subList(1, levels.size)
            return distances.all { it in 1..3 }
        }
    }

    fun countSafeReports(input: String): Int {
        val reports = extractReports(input)

        return reports.count { (it.isIncreasing() || it.isDecreasing()) && it.isStable() }
    }

    fun extractReports(input: String): List<Report> = input.split("\n")
        .map { it.trim() }
        .filterNot { it.isEmpty() }
        .map { line -> Report(line.split(" ").map { it.toInt() }) }
        .toList()
}