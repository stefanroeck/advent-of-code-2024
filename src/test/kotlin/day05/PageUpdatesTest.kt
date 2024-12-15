package day05

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val SAMPLE_INPUT = """
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
"""

class PageUpdatesTest {
    @Test
    fun `parse rules and updates`() {
        val input = SAMPLE_INPUT.trimIndent()

        val instructions: Instructions = PageUpdates.parseInput(input)

        assertEquals(21, instructions.rules.size)
        assertEquals(Rule(47, 53), instructions.rules.first())
        assertEquals(Rule(53, 13), instructions.rules.last())

        assertEquals(6, instructions.updates.size)
        assertEquals(PageUpdate(listOf(75, 47, 61, 53, 29)), instructions.updates.first())
        assertEquals(PageUpdate(listOf(97, 13, 75, 29, 47)), instructions.updates.last())
    }

    @Test
    fun `correct updates`() {
        val input = SAMPLE_INPUT.trimIndent()
        val instructions = PageUpdates.parseInput(input)

        val expectedCorrectUpdates = listOf(
            PageUpdate(listOf(75, 47, 61, 53, 29)),
            PageUpdate(listOf(97, 61, 53, 29, 13)),
            PageUpdate(listOf(75, 29, 13)),
        )

        assertEquals(expectedCorrectUpdates, instructions.correctUpdates())
    }

    @Test
    fun `sum of middle page numbers`() {
        val input = SAMPLE_INPUT.trimIndent()
        val instructions = PageUpdates.parseInput(input)

        assertEquals(143, instructions.sumOfMiddlePageNumbersForCorrectUpdates())
    }

}