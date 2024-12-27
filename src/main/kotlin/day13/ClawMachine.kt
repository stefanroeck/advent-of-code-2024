package day13

import util.MapOfThings.Point
import util.MapOfThings.Vector
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.max

data class Button(val name: String, val deltaX: Int, val deltaY: Int, val tokenCosts: Int) {
    fun vector() = Vector(deltaX, deltaY)
}

data class Price(val x: Int, val y: Int)

private val buttonRegex = Regex("""^Button ([AB]): X\+(\d+), Y\+(\d+)$""")
private val priceButtonRegex = Regex("""^Prize: X=(\d+), Y=(\d+)$""")

private const val MAX_BUTTON_PUSHES = 100

class ClawMachine(private val lines: List<String>) {

    private val recursionCounter = AtomicLong(0)

    fun calculateCostsForFirstMachine(): Int? {
        val (buttonA, buttonB, price) = parseMachineSpec(lines)

        return calculateCosts(price, buttonA, buttonB)
    }

    fun calculateCostsForSolvableMachines(): Int {
        return lines.filterNot { it.isEmpty() }.chunked(3).sumOf { singleMachineLines ->
            val (buttonA, buttonB, price) = parseMachineSpec(singleMachineLines)

            calculateCosts(price, buttonA, buttonB) ?: 0
        }
    }

    private fun parseMachineSpec(singleMachineLines: List<String>): Triple<Button, Button, Price> {
        val buttonLines = singleMachineLines.mapNotNull { buttonRegex.matchEntire(it) }
        val buttonA = buttonLines.first { it.groupValues[1] == "A" }.groupValues.let {
            Button(it[1], it[2].toInt(), it[3].toInt(), 3)
        }
        val buttonB = buttonLines.first { it.groupValues[1] == "B" }.groupValues.let {
            Button(it[1], it[2].toInt(), it[3].toInt(), 1)
        }
        val price = singleMachineLines.firstNotNullOf { priceButtonRegex.matchEntire(it) }.groupValues.let {
            Price(it[1].toInt(), it[2].toInt())
        }
        return Triple(buttonA, buttonB, price)
    }

    private fun calculateCosts(price: Price, buttonA: Button, buttonB: Button): Int? {
        val start = Point(0, 0)
        val target = Point(price.x, price.y)

        if (checkTargetCoordinatesNotReachableWithMaxCosts(buttonA, buttonB, target)) {
            return null
        }

        return navigateToTargetRecursively(start, target, mutableMapOf(buttonA to 0, buttonB to 0), 0)
    }

    private fun checkIsTargetGradientsOutsideOfRange(
        start: Point,
        buttonA: Button,
        buttonB: Button,
        target: Point
    ): Boolean {
        val gradientVectorA = Point.gradient(start, start.translate(Vector(buttonA.deltaX, buttonA.deltaY)))
        val gradientVectorB = Point.gradient(start, start.translate(Vector(buttonB.deltaX, buttonB.deltaY)))
        val gradientTarget = Point.gradient(start, target)

        var targetOutsideRange = false
        with(listOf(gradientVectorA, gradientVectorB)) {
            if (all { gradientTarget > it } || all { gradientTarget < it }) {
                //println("Target price unreachable with buttons. $gradientTarget outside of ${this.sorted()}")
                targetOutsideRange = true
            }
        }
        return targetOutsideRange
    }

    private fun checkTargetCoordinatesNotReachableWithMaxCosts(
        buttonA: Button,
        buttonB: Button,
        target: Point
    ): Boolean {
        val maximumReachablePoint = Point(
            max(buttonA.deltaX, buttonB.deltaX) * MAX_BUTTON_PUSHES,
            max(buttonA.deltaY, buttonB.deltaY) * MAX_BUTTON_PUSHES
        )

        if (target > maximumReachablePoint) {
            println("Target $target is beyond max reachable point $maximumReachablePoint")
            return true
        }
        return false
    }

    private fun navigateToTargetRecursively(
        start: Point, target: Point, buttonsWithPushes: Map<Button, Int>, costs: Int
    ): Int? {
        if (start == target) {
            println("Found target $start")
            return costs
        }

        if (start.col >= target.col || start.row >= target.row) {
            return null
        }

        if (buttonsWithPushes.any { it.value >= MAX_BUTTON_PUSHES }) {
            return null
        }

        val buttonA = buttonsWithPushes.keys.first { it.name == "A" }
        val buttonB = buttonsWithPushes.keys.first { it.name == "B" }
        if (checkIsTargetGradientsOutsideOfRange(start, buttonA, buttonB, target)) {
            return null
        }

        if (recursionCounter.incrementAndGet() % 1_000_000L == 0L) {
            println(
                "Checking $start at current costs $costs and target $target with pushes: ${
                    buttonsWithPushes.entries.joinToString { it.key.name + "=" + it.value }
                }"
            )
        }

        buttonsWithPushes.forEach { (button) ->
            val result = navigateToTargetRecursively(
                start.translate(button.vector()),
                target,
                buttonsWithPushes.map { it.key to it.value + if (button == it.key) 1 else 0 }.toMap(),
                costs + button.tokenCosts
            )
            if (result != null) {
                return result
            }
        }
        return null
    }


}