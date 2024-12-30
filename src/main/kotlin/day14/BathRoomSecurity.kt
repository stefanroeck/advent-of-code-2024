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

        val middleRow = height / 2L
        val middleCol = width / 2L
        val quadrants = listOf(
            Quadrant(topLeft = Point(0, 0), bottomRight = Point(middleCol - 1, middleRow - 1)),
            Quadrant(topLeft = Point(middleCol + 1, 0), bottomRight = Point(width - 1, middleRow - 1)),
            Quadrant(topLeft = Point(0, middleRow + 1), bottomRight = Point(middleCol - 1, height - 1)),
            Quadrant(topLeft = Point(middleCol + 1, middleRow + 1), bottomRight = Point(width - 1, height - 1)),
        )

        val robotsByQuadrants = robots
            .filter { it.position.row != middleRow && it.position.col != middleCol }
            .groupBy {
                quadrants.single { q ->
                    q contains it.position
                }
            }

        // multiple count of each quadrant
        return robotsByQuadrants.entries.map { it.value.size }.fold(1) { prev, curr -> prev * curr }
    }

    private fun moveRobot(robot: Robot) = robot.moveOnMap(this.width, this.height)

}