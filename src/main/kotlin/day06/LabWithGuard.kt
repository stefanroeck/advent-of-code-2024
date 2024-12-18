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

enum class MapMarker(val symbol: Char) {
    Obstacle('#'),
    EmptyTile('.'),
    LoopTrapObstruction('O'),
    ;

    fun isObstacle() = listOf(Obstacle, LoopTrapObstruction).any { it == this }

    companion object {
        fun markerFor(symbol: Char): MapMarker {
            return MapMarker.entries.first { it.symbol == symbol }
        }
    }
}

data class LabMap(
    private val points: Map<Point, MapMarker>,
    val width: Int,
    val height: Int,
    val guardLocation: Point?,
    val guardDirection: Direction,
    private val visitedPoints: Set<Point> = emptySet(),
) {
    fun markerAt(point: Point) = points[point] ?: throw IllegalArgumentException("Point $point outside of map")

    internal fun moveGuard(): LabMap {
        if (guardLocation == null) throw IllegalStateException("Cannot move guard as he left the map.")

        val nextLocation = guardLocation.apply(guardDirection.vector)
        if (!isOnMap(nextLocation)) {
            // leave map
            return this.copy(guardLocation = null, visitedPoints = visitedPoints + guardLocation)
        } else if (markerAt(nextLocation).isObstacle()) {
            // rotate
            val newGuardDirection = guardDirection.rotate()
            return this.copy(guardDirection = newGuardDirection)
        } else {
            // step forward
            return this.copy(guardLocation = nextLocation, visitedPoints = visitedPoints + guardLocation)
        }
    }

    private fun isOnMap(point: Point): Boolean = point.x in 0..<width && point.y in 0..<height

    internal fun transformMap(replacements: Map<Point, MapMarker>): LabMap {
        val newPoints = points.toMutableMap()
        replacements.forEach { (point, marker) -> newPoints[point] = marker }
        return this.copy(points = newPoints)
    }

    fun visitedLocations() = visitedPoints
    fun obstructions() = points.filter { it.value == MapMarker.LoopTrapObstruction }.keys
}

object LabWithGuard {
    fun buildLabMap(lines: List<String>): LabMap {
        var guardLocation: Point? = null
        val points = lines.flatMapIndexed { y, line ->
            line.mapIndexed { x, char ->
                val mapChar = if (char == '^') {
                    guardLocation = Point(x, y)
                    MapMarker.EmptyTile.symbol
                } else {
                    char
                }
                Point(x, y) to MapMarker.markerFor(mapChar)
            }
        }.toMap()
        return LabMap(
            points,
            width = lines[0].length,
            height = lines.size,
            guardLocation = guardLocation,
            guardDirection = Direction.Up
        )
    }

    fun findLoopTrapObstructions(map: LabMap): Set<Point> {
        // run through complete map to have visited paths
        // then run again after setting an obstruction on every visited marker and check for loops
        val initialMap = map
        val visitedMap = sendGuardOnPatrol(initialMap)
        val visitedLocations = visitedMap.visitedLocations().filterNot { it == initialMap.guardLocation }

        println("Trying traps for ${visitedLocations.size} locations in a ${map.width}x${map.height} map")
        val resultListWithObstructions: List<Point> = visitedLocations.flatMapIndexed { i, visitedPoint ->
            val searchMap =
                initialMap.transformMap(mapOf(visitedPoint to MapMarker.LoopTrapObstruction))
            val guardTrapped = sendGuardUntilEntersLoop(searchMap)
            if (guardTrapped.isPresent) {
                println("  > Obstruction at $visitedPoint")
                println("  > ${Math.round((i.toDouble() / visitedLocations.size.toDouble()) * 100)}% done")
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
            if (newGuardLocation != null && previousMap.visitedLocations().contains(newGuardLocation)) {
                val guardDirection = newMapAfterMovement.guardDirection
                val newVisitCount =
                    pointsToVisitCount.compute(newGuardLocation) { _, old -> if (old == null) 1 else old + 1 }
                if (newVisitCount != null && newVisitCount > 4) {
                    // Visits might happen multiple times for different orientations (right, down), but not twice the same direction
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
