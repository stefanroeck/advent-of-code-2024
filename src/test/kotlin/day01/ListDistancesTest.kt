package day01

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val SAMPLE_INPUT = """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """

class ListDistancesTest {

    @Test
    fun `test distance for sample`(){
        val input = SAMPLE_INPUT.trimIndent()

        val distance: Int = ListDistances.calculateDistances(input)

        assertEquals(11, distance)
    }

    @Test
    fun `test similarity for sample`() {
        val input = SAMPLE_INPUT.trimIndent()

        val similarity: Int = ListDistances.calculateSimilarity(input)

        assertEquals(31, similarity)
    }
}