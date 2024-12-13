package day01

object ListDistances {
    fun calculateDistances(input: String): Int {
        val (firstList, secondList) = parseInputToLists(input)
        return firstList
            .mapIndexed { idx, first -> Pair(first, secondList[idx]) }
            .fold(0) {acc, p -> acc + Math.abs(p.first - p.second)}
    }

    fun calculateSimilarity(input: String): Int {
        val (firstList, secondList) = parseInputToLists(input)
        return firstList
            .mapIndexed { idx, first -> Pair(first, secondList[idx]) }
            .fold(0) {acc, p -> acc + p.first * secondList.filter { p.first == it }.size}
    }

    private fun parseInputToLists(input: String): Pair<List<Int>, List<Int>> {
        val elements = input
            .split("\n")
            .map { it.trim() }
            .filterNot { it.isEmpty() }
            .map { it.split("   ") }
            .map { Pair(it[0].trim().toInt(), it[1].trim().toInt()) }
            .toList()
        val firstList = elements.map { it.first }.sorted()
        val secondList = elements.map { it.second }.sorted()

        assert(firstList.size == secondList.size) { "List don't have the same size" }
        return Pair(firstList, secondList)
    }

}
