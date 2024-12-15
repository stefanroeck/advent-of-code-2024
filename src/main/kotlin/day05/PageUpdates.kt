package day05

import java.util.Collections

data class Rule(val firstPage: Int, val secondPage: Int)

data class PageUpdate(val pages: List<Int>) {

    fun isValid(rules: List<Rule>): Boolean {
        val relevantRules = relevantRules(rules)
        return relevantRules.all { satisfies(it) }
    }

    private fun satisfies(rule: Rule) = pages.indexOf(rule.firstPage) < pages.indexOf(rule.secondPage)

    private fun relevantRules(rules: List<Rule>) =
        rules.filter { rule -> pages.contains(rule.firstPage) && pages.contains(rule.secondPage) }

    fun middlePageNumber(): Int {
        return pages[pages.size / 2]
    }

    fun fixOrder(rules: List<Rule>): PageUpdate {
        val relevantRules = relevantRules(rules)
        var result = this
        // loop until all rules are fixed
        do {
            val nonSatisfiedRule = relevantRules.filterNot { result.satisfies(it) }.firstOrNull()
            if (nonSatisfiedRule != null) {
                result = PageUpdate(result.fixRule(nonSatisfiedRule))
            }
        } while (nonSatisfiedRule != null)
        return result
    }

    private fun fixRule(rule: Rule): List<Int> {
        val result = pages.toMutableList()
        Collections.swap(result, pages.indexOf(rule.firstPage), pages.indexOf(rule.secondPage))
        return result.toList()
    }
}

data class Instructions(val rules: List<Rule>, val updates: List<PageUpdate>) {
    fun correctUpdates() = updates.filter { it.isValid(rules) }

    private fun incorrectUpdates() = updates.filterNot { it.isValid(rules) }

    fun sumOfMiddlePageNumbersForCorrectUpdates(): Int {
        return correctUpdates().sumOf(PageUpdate::middlePageNumber)
    }

    fun sumOfMiddlePageNumbersForIncorrectUpdates(): Int {
        return incorrectUpdates()
            .map { it.fixOrder(rules) }
            .sumOf(PageUpdate::middlePageNumber)
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