package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

const val SAMPLE_INPUT = """xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"""

class MultiplierTest {
    @Test
    fun `multiply sample input`() {
        assertEquals(161, Multiplier.multiply(SAMPLE_INPUT.trimIndent()))
    }
}