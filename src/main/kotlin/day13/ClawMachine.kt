package day13

import util.MapOfThings.Point
import util.MapOfThings.Vector

data class Button(val name: String, val deltaX: Int, val deltaY: Int, val tokenCosts: Int) {
    fun vector() = Vector(deltaX, deltaY)
}

data class Price(val x: Int, val y: Int)

private val buttonRegex = Regex("""^Button ([AB]): X\+(\d+), Y\+(\d+)$""")
private val priceButtonRegex = Regex("""^Prize: X=(\d+), Y=(\d+)$""")

private const val MAX_BUTTON_PUSHES = 100

class ClawMachine(private val lines: List<String>) {

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

        // order of buttonA and buttonB doesn't matter, so walk along
        // buttonA vector until the vector from current point to target point equals buttonB vector

        val targetVectorB = buttonB.vector().reduce()

        var vectorToTarget = Point.vector(start, target)
        var currentPoint = start
        var costs = 0
        var iterations = 0

        while (vectorToTarget != targetVectorB) {
            currentPoint = currentPoint.translate(buttonA.vector())
            costs += buttonA.tokenCosts
            vectorToTarget = Point.vector(currentPoint, target).reduce()

            if (iterations++ >= MAX_BUTTON_PUSHES || vectorToTarget.dx < 0 || vectorToTarget.dy < 0) {
                // escape function, no solution
                return null
            }
        }

        while (currentPoint != target) {
            currentPoint = currentPoint.translate(buttonB.vector())
            costs += buttonB.tokenCosts
        }

        return costs
    }
}