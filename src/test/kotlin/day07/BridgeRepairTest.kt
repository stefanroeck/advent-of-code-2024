package day07

import util.InputUtils
import kotlin.test.Test
import kotlin.test.assertEquals


private const val SAMPLE_INPUT = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20 
"""

class BridgeRepairTest {

    @Test
    fun `test sample`() {
        val sum = BridgeRepair.findOperatorsAndCalcSum(InputUtils.parseLines(SAMPLE_INPUT))

        assertEquals(3749, sum)
    }

}