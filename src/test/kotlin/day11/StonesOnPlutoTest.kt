package day11

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.Test
import kotlin.test.assertEquals

class StonesOnPlutoTest {

    @ParameterizedTest
    @CsvSource(
        ignoreLeadingAndTrailingWhitespace = true, textBlock = """
          '125 17',             '253000 1 7',
          '253000 1 7',         '253 0 2024 14168',
          '253 0 2024 14168',   '512072 1 20 24 28676032'"""
    )
    fun `blinking once`(input: String, expected: String) {
        val result: String = StonesOnPluto.blink(input, 1)

        assertEquals(expected, result)
    }

    @Test
    fun `multiple blinks on sample input`() {
        val result = StonesOnPluto.blink("125 17", 6)

        assertEquals("2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2", result)
    }

    @Test
    fun `even more blinks on sample input`() {
        val input = "125 17"
        val result = StonesOnPluto.blink(input, 25)

        assertEquals(55312, result.split(" ").size)
        assertEquals(55312, StonesOnPluto.countBlinks(input, 25))
    }

}