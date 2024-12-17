package day06

import java.util.Optional


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
    LoopTrapObstruction('O'),
    ;

    fun isGuard() = listOf(GuardUp, GuardRight, GuardDown, GuardLeft).any { it == this }
    fun isObstacle() = listOf(Obstacle, LoopTrapObstruction).any { it == this }
    fun isVisited() = listOf(Visited).any { it == this }

    fun guardDirection() = guardDirectionMapping.entries.first { it.value == this }.key

    companion object {
        fun markerFor(symbol: Char): MapMarker {
            return MapMarker.entries.first { it.symbol == symbol }
        }

        fun guardDirection(direction: Direction) =
            guardDirectionMapping.entries.first { it.key == direction }.value
    }
}

data class LabMap(
    private val points: Map<Point, MapMarker>,
    val width: Int,
    val height: Int,
    val guardLocation: Point? = points.filter { it.value.isGuard() }.map { it.key }.firstOrNull()
) {
    fun markerAt(point: Point) = points[point] ?: throw IllegalArgumentException("Point $point outside of map")

    internal fun moveGuard(): LabMap {
        if (guardLocation != null) {
            val guardMarker = markerAt(guardLocation)
            val guardDirection = guardMarker.guardDirection()
            val nextLocation = guardLocation.apply(guardDirection.vector)
            if (!isOnMap(nextLocation)) {
                // leave map
                return transformMap(mapOf(guardLocation to MapMarker.Visited), newGuardLocation = null)
            } else if (markerAt(nextLocation).isObstacle()) {
                // rotate
                val newGuardDirection = guardDirection.rotate()
                val newGuardMarker = MapMarker.guardDirection(newGuardDirection)
                return transformMap(mapOf(guardLocation to newGuardMarker), newGuardLocation = guardLocation)
            } else {
                // step forward
                return transformMap(
                    mapOf(guardLocation to MapMarker.Visited, nextLocation to guardMarker),
                    newGuardLocation = nextLocation
                )
            }
        }
        return this
    }

    private fun isOnMap(point: Point): Boolean = point.x in 0..<width && point.y in 0..<height

    internal fun transformMap(replacements: Map<Point, MapMarker>, newGuardLocation: Point?): LabMap {
        val newPoints = points.toMutableMap()
        replacements.forEach { (point, marker) -> newPoints[point] = marker }
        return this.copy(points = newPoints.toMap(), guardLocation = newGuardLocation)
    }

    fun visitedLocations() = points.filter { it.value.isVisited() }.keys
    fun obstructions() = points.filter { it.value == MapMarker.LoopTrapObstruction }.keys
}

object LabWithGuard {
    fun buildLabMap(lines: List<String>): LabMap {
        val points = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char -> Point(x, y) to MapMarker.markerFor(char) }
        }.toMap()
        return LabMap(points, width = lines[0].length, height = lines.size)
    }

    fun findLoopTrapObstructions(map: LabMap): Set<Point> {
        // run through complete map to have visited paths
        // then run again after setting an obstruction on every visited marker and check for loops
        val initialMap = map
        val visitedMap = sendGuardOnPatrol(initialMap)
        val visitedLocations = visitedMap.visitedLocations().filterNot { it == initialMap.guardLocation }

        println("Trying traps for ${visitedLocations.size} locations")
        val resultListWithObstructions: List<Point> = visitedLocations.flatMap { visitedPoint ->
            val searchMap =
                initialMap.transformMap(mapOf(visitedPoint to MapMarker.LoopTrapObstruction), initialMap.guardLocation)
            val guardTrapped = sendGuardUntilEntersLoop(searchMap)
            if (guardTrapped.isPresent) {
                println("  > Obstruction at $visitedPoint")
                listOf(visitedPoint)
            } else {
                emptyList()
            }
        }

        return resultListWithObstructions.toSet()
    }

    internal fun sendGuardUntilEntersLoop(map: LabMap): Optional<Point> {
        // moving on two previously visited tiles indicate a loop
        var previousMap = map
        val pointsToVisitCount = mutableMapOf<Point, Int>()
        do {
            val newMapAfterMovement = previousMap.moveGuard()
            val newGuardLocation = newMapAfterMovement.guardLocation
            if (newGuardLocation != null && previousMap.markerAt(newGuardLocation).isVisited()) {
                val guardDirection = newMapAfterMovement.markerAt(newGuardLocation).guardDirection()
                val newVisitCount =
                    pointsToVisitCount.compute(newGuardLocation) { _, old -> if (old == null) 1 else old + 1 }
                if (newVisitCount != null && newVisitCount > 2) {
                    // Visiting twice might happen for different orientations (right, down), but once more --> loop detected
                    println("Loop detected with guard at $newGuardLocation looking $guardDirection")
                    return Optional.of(newGuardLocation)
                }
            }
            previousMap = newMapAfterMovement
        } while (previousMap.guardLocation != null)
        return Optional.empty()
    }

    fun sendGuardOnPatrol(map: LabMap): LabMap {
        var updatedLabMap = map
        do {
            updatedLabMap = updatedLabMap.moveGuard()
        } while (updatedLabMap.guardLocation != null)

        return updatedLabMap
    }


}
