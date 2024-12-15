package day06


data class Point(val x: Int, val y: Int) {
    fun apply(vector: Vector) = Point(this.x + vector.dx, this.y + vector.dy)
}

data class Vector(val dx: Int, val dy: Int)

enum class Direction(val vector: Vector) {
    Up(Vector(0, -1)),
    Right(Vector(1, 0)),
    Down(Vector(0, 1)),
    Left(Vector(-1, 0)),
    ;

    fun rotate(): Direction = when (this) {
        Up -> Right
        Right -> Down
        Down -> Left
        Left -> Up
    }
}

// Is there sth like a Typescript Record<>?
private val guardDirectionMapping = mapOf(
    Direction.Up to MapMarker.GuardUp,
    Direction.Right to MapMarker.GuardRight,
    Direction.Down to MapMarker.GuardDown,
    Direction.Left to MapMarker.GuardLeft,
)

enum class MapMarker(val symbol: Char) {
    Obstacle('#'),
    EmptyTile('.'),
    GuardUp('^'),
    GuardRight('>'),
    GuardDown('v'),
    GuardLeft('<'),
    Visited('X'),
    ;

    fun isGuard() = listOf(GuardUp, GuardRight, GuardDown, GuardLeft).any { it == this }

    fun guardDirection() = guardDirectionMapping.entries.first { it.value == this }.key

    companion object {
        fun markerFor(symbol: Char): MapMarker {
            return MapMarker.entries.first { it.symbol == symbol }
        }

        fun guardDirection(direction: Direction) =
            guardDirectionMapping.entries.first { it.key == direction }.value
    }
}

private const val MAX_STEPS = 10_000

data class LabMap(private val points: Map<Point, MapMarker>, val width: Int, val height: Int) {
    fun markerAt(point: Point) = points[point] ?: throw IllegalArgumentException("Point $point outside of map")

    fun guardLocation() = points.filter { it.value.isGuard() }.map { it.key }.firstOrNull()

    fun sendGuardOnPatrol(): LabMap {
        var updatedLabMap = this
        var steps = 0 // avoid endless cycles
        do {
            updatedLabMap = updatedLabMap.moveGuard()
            steps++
        } while (updatedLabMap.guardLocation() != null && steps <= MAX_STEPS)

        check(steps != MAX_STEPS) { "Guard got stuck in a cycle. Terminating after $MAX_STEPS steps." }
        return updatedLabMap
    }

    private fun moveGuard(): LabMap {
        val guardLocation = guardLocation()
        if (guardLocation != null) {
            val guardMarker = markerAt(guardLocation)
            val guardDirection = guardMarker.guardDirection()
            val nextLocation = guardLocation.apply(guardDirection.vector)
            if (!isOnMap(nextLocation)) {
                // leave map
                return replaceOnMap(mapOf(guardLocation to MapMarker.Visited))
            } else if (markerAt(nextLocation) == MapMarker.Obstacle) {
                // rotate
                val newGuardDirection = guardDirection.rotate()
                val newGuardMarker = MapMarker.guardDirection(newGuardDirection)
                return replaceOnMap(mapOf(guardLocation to newGuardMarker))
            } else {
                // step forward
                return replaceOnMap(mapOf(guardLocation to MapMarker.Visited, nextLocation to guardMarker))
            }
        }
        return this
    }

    private fun isOnMap(point: Point): Boolean = point.x in 0..<width && point.y in 0..<height

    private fun replaceOnMap(replacements: Map<Point, MapMarker>): LabMap {
        val newPoints = points.toMutableMap()
        replacements.forEach { (point, marker) -> newPoints[point] = marker }
        return this.copy(points = newPoints.toMap())
    }

    fun visitedLocations() = points.count { it.value == MapMarker.Visited }
}

object LabWithGuard {
    fun buildLabMap(lines: List<String>): LabMap {
        val points = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char -> Point(x, y) to MapMarker.markerFor(char) }
        }.toMap()
        return LabMap(points, width = lines[0].length, height = lines.size)
    }
}
