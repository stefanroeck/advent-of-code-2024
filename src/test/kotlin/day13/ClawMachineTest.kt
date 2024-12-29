package day13

import org.junit.jupiter.api.Test
import util.InputUtils
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClawMachineTest {

    @Test
    fun `test first machine`() {
        val input = """
         Button A: X+94, Y+34
         Button B: X+22, Y+67
         Prize: X=8400, Y=5400
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertEquals(280, tokenCosts)
    }

    @Test
    fun `simple machine without solution`() {
        val input = """
         Button A: X+1, Y+5
         Button B: X+5, Y+2
         Prize: X=15, Y=14
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertNull(tokenCosts)
    }

    @Test
    fun `second machine has no solution`() {
        val input = """
         Button A: X+26, Y+66
         Button B: X+67, Y+21
         Prize: X=12748, Y=12176
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertNull(tokenCosts)
    }

    @Test
    fun `third machine has solution`() {
        val input = """
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertEquals(200, tokenCosts)
    }

    @Test
    fun `fourth machine has no solution`() {
        val input = """
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertNull(tokenCosts)
    }

    @Test
    fun `machine with high values (and no solution)`() {
        val input = """
            Button A: X+46, Y+78
            Button B: X+47, Y+19
            Prize: X=10323, Y=5047
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))
        val tokenCosts = clawMachine.calculateCostsForFirstMachine()

        assertNull(tokenCosts)
    }

    @Test
    fun `sum of prices for multiple machines`() {
        val input = """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent()

        val clawMachine = ClawMachine(InputUtils.parseLines(input))

        assertEquals(480, clawMachine.calculateCostsForSolvableMachines())
    }
}