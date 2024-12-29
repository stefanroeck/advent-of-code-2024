package day13

import util.MapOfThings.Point
import util.MapOfThings.Vector
import java.math.BigDecimal
import java.math.MathContext
import kotlin.math.abs
import kotlin.math.round

data class Button(val name: String, val deltaX: Int, val deltaY: Int, val tokenCosts: Int) {
    fun vector() = Vector(deltaX, deltaY)
}

data class Price(val x: Long, val y: Long)

private val buttonRegex = Regex("""^Button ([AB]): X\+(\d+), Y\+(\d+)$""")
private val priceButtonRegex = Regex("""^Prize: X=(\d+), Y=(\d+)$""")


class ClawMachine(private val lines: List<String>) {

    fun calculateCostsForFirstMachine(): Long? {
        val (buttonA, buttonB, price) = parseMachineSpec(lines)

        return calculateCosts(price, buttonA, buttonB)
    }

    fun calculateCostsForSolvableMachines(): Long {
        return lines.filterNot { it.isEmpty() }.chunked(3).sumOf { singleMachineLines ->
            val (buttonA, buttonB, price) = parseMachineSpec(singleMachineLines)

            calculateCosts(price, buttonA, buttonB) ?: 0
        }
    }

    fun calculateCostsForSolvableMachinesWithCorrectedPriceCoordinates(): Long {
        return lines.filterNot { it.isEmpty() }.chunked(3).sumOf { singleMachineLines ->
            val (buttonA, buttonB, price) = parseMachineSpec(singleMachineLines)
            val fixedPrice = Price(price.x + 10_000_000_000_000, price.y + 10_000_000_000_000)
            calculateCosts(fixedPrice, buttonA, buttonB).also { println("Returned a price: $it") } ?: 0
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
            Price(it[1].toLong(), it[2].toLong())
        }
        return Triple(buttonA, buttonB, price)
    }

    private fun calculateCosts(price: Price, buttonA: Button, buttonB: Button): Long? {
        val target = Point(price.x, price.y)
        val vectorA = buttonA.vector()
        val vectorB = buttonB.vector()

        // function f(x) = ax + b
        // fn buttonA through start (0; 0): f(x) = (a.dy/a.dx)x
        // fn buttonB through price (p.x; p.y): p.y = (b.dy/b.dx)*p.x + b
        //   b = p.y - (b.dy/b.dx)*p.x
        // fn buttonB: f(x) = (b.dy/b.dx)*x + (p.y - (b.dy/b.dx)*p.x)

        // calc intersection point, if whole number, --> solution
        // intersection where y1 = y2
        // m1x + b1 = m2x + b2
        // (a.dy/a.dx)x = (b.dy/b.dx)x + (p.y - (b.dy/b.dx)*p.x)
        // x(a.dy/a.dx - b.dy/b.dx) = p.y - (b.dy/b.dx)*p.x
        // x = (p.y - (b.dy/b.dx)*p.x) / (a.dy/a.dx - b.dy/b.dx)
        // y = (a.dy/a.dx)x

        val intersectionX =
            div(
                (target.row.toBigDecimal() - div(
                    vectorB.dy,
                    vectorB.dx
                ) * target.col.toBigDecimal()), (div(vectorA.dy, vectorA.dx) - div(vectorB.dy, vectorB.dx))
            )
        val intersectionY = div(vectorA.dy, vectorA.dx) * intersectionX

        val intersectionPoint =
            if (isWholeNumber(intersectionX) && isWholeNumber(intersectionY)) Point(
                round(intersectionX.toDouble()).toLong(),
                round(intersectionY.toDouble()).toLong()
            ) else null

        if (intersectionPoint != null) {
            // using x-coordinates only works if dy is never null
            check(vectorA.dy != 0L)
            check(vectorB.dy != 0L)
            val buttonAPushes = intersectionPoint.col / vectorA.dx
            val buttonBPushes = (target.col - intersectionPoint.col) / vectorB.dx
            // calc costs
            return buttonAPushes * buttonA.tokenCosts + buttonBPushes * buttonB.tokenCosts
        }

        return null
    }

    private fun isWholeNumber(n: BigDecimal) = abs(n.toDouble() - round(n.toDouble())) < 0.00001

    private fun div(a: Long, b: Long): BigDecimal = a.toBigDecimal().divide(b.toBigDecimal(), MathContext.DECIMAL128)
    private fun div(a: BigDecimal, b: BigDecimal): BigDecimal = a.divide(b, MathContext.DECIMAL128)

}