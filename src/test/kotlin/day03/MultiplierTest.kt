package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

const val SAMPLE_INPUT_PART1 = """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""
const val SAMPLE_INPUT_PART2 = """xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"""

class MultiplierTest {
    @Test
    fun `multiply sample input part 1`() {
        assertEquals(161, Multiplier.multiply(SAMPLE_INPUT_PART1.trimIndent()))
    }

    @Test
    fun `multiply sample input part 2 with do and dont`() {
        assertEquals(48, Multiplier.multiplyWithDoAndDont(SAMPLE_INPUT_PART2.trimIndent()))
    }

}