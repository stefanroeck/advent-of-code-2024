package day14

import util.MapOfThings.Point
import util.MapOfThings.Vector

private data class Robot(var position: Point, val movement: Vector) {
    fun moveOnMap(width: Long, height: Long) {
        position = position.translateWithinBounds(movement, width, height)
    }
}

private data class Quadrant(val topLeft: Point, val bottomRight: Point) {
    infix fun contains(point: Point) = point.within(topLeft, bottomRight)
}

private data class Quadrants(val width: Long, val height: Long) {

    val middleRow: Long
        get() =
            height / 2

    val middleCol: Long
        get() =
            width / 2

    val quadrants: List<Quadrant>
        get() =
            listOf(
                Quadrant(topLeft = Point(0, 0), bottomRight = Point(middleCol - 1, middleRow - 1)),
                Quadrant(topLeft = Point(middleCol + 1, 0), bottomRight = Point(width - 1, middleRow - 1)),
                Quadrant(topLeft = Point(0, middleRow + 1), bottomRight = Point(middleCol - 1, height - 1)),
                Quadrant(topLeft = Point(middleCol + 1, middleRow + 1), bottomRight = Point(width - 1, height - 1)),
            )
}

private val robotLineRegex = Regex("""^p=([-\d]+),([-\d]+) v=([-\d]+),([-\d]+)$""")

class BathRoomSecurity(private val lines: List<String>, private val width: Long, private val height: Long) {

    private fun parseRobotPositions(lines: List<String>): List<Robot> {
        return lines
            .mapNotNull { robotLineRegex.matchEntire(it) }
            .map { it.groupValues }
            .map { it.takeLast(4).map { s -> s.toInt() } }
            .map { Robot(Point(it[0], it[1]), Vector(it[2], it[3])) }
    }

    fun predictMovement(steps: Int): Long {
        val robots = parseRobotPositions(lines)

        (0..<steps).forEach { _ ->
            robots.forEach { moveRobot(it) }
        }

        check(width % 2L == 1L) { "width is expected to be uneven" }
        check(height % 2L == 1L) { "height is expected to be uneven" }

        val bathRoom = Quadrants(width, height)

        val robotsByQuadrants = robots
            .filter { it.position.row != bathRoom.middleRow && it.position.col != bathRoom.middleCol }
            .groupBy {
                bathRoom.quadrants.single { q ->
                    q contains it.position
                }
            }

        // multiple count of each quadrant
        return robotsByQuadrants.entries.map { it.value.size }.fold(1) { prev, curr -> prev * curr }
    }

    fun findChristmasTree(startAtSteps: Int = 1000, stopAtSteps: Int = 10000) {
        var loops = 0
        val robots = parseRobotPositions(lines)
        (0..<startAtSteps).forEach { _ ->
            robots.forEach { moveRobot(it) }
            loops++
        }

        val blankLine = ".".repeat(width.toInt())
        val suspicousLines = mutableMapOf<Int, Int>()

        (startAtSteps..stopAtSteps).forEach { idx ->
            loops++
            robots.forEach { moveRobot(it) }
            // look for symmetries, as a x-mas tree is expected to be symmetric ;-)
            val suspiciousLines = (0..<height).filter { row ->
                val line = (0..<width).joinToString("") { col ->
                    val count = robots.count { it.position == Point(col, row) }
                    if (count > 0) "x" else "."
                }
                if (line != blankLine) {
                    line.contains("x".repeat(7))
                } else {
                    false
                }
            }

            if (suspiciousLines.count() > 3) {
                println("Found easter egg after $loops seconds: $suspiciousLines")
                printRobotPositions(robots)
                suspicousLines.put(loops, suspiciousLines.size)
            }
        }

        val sortedEntries = suspicousLines.toList().sortedByDescending { (_, value) -> value }.toMap()
        println(sortedEntries)
    }

    private fun printRobotPositions(robots: List<Robot>) {
        println("")
        (0..<height).forEach { row ->
            (0..<width).forEach { col ->
                val robotCount = robots.count { it.position == Point(col, row) }
                print(if (robotCount > 0) "x" else ".")
            }
            print("\n")
        }
    }

    private fun moveRobot(robot: Robot) = robot.moveOnMap(this.width, this.height)

}