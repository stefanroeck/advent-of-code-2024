package day05

data class Rule(val firstPage: Int, val secondPage: Int)

data class PageUpdate(val pages: List<Int>) {

    fun isValid(rules: List<Rule>): Boolean {
        val relevantRules =
            rules.filter { rule -> pages.contains(rule.firstPage) && pages.contains(rule.secondPage) }
        return relevantRules.all { pages.indexOf(it.firstPage) < pages.indexOf(it.secondPage) }
    }

    fun middlePageNumber(): Int {
        return pages[pages.size / 2]
    }
}

data class Instructions(val rules: List<Rule>, val updates: List<PageUpdate>) {
    fun correctUpdates(): List<PageUpdate> {
        return updates.filter { update ->
            update.isValid(rules)
        }
    }

    fun sumOfMiddlePageNumbersForCorrectUpdates(): Int {
        return correctUpdates().sumOf(PageUpdate::middlePageNumber)
    }
}

object PageUpdates {
    fun parseInput(input: String): Instructions {
        val lines = input.split("\n").map { it.trim() }.filterNot { it.isEmpty() }

        val rules = lines
            .filter { it.contains("|") }
            .map { it.split("|") }
            .map { Rule(it[0].toInt(), it[1].toInt()) }

        val updates = lines
            .filter { it.contains(",") }
            .map { it.split(",") }
            .map { line -> PageUpdate(line.map { it.toInt() }) }

        return Instructions(rules, updates)
    }
}