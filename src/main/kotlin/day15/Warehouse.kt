package day15

import day15.MapType.NORMAL
import day15.MapType.WIDE
import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Point

sealed interface WarehouseBlock

data object Wall : WarehouseBlock

data object Box : WarehouseBlock

data object LeftBox : WarehouseBlock
data object RightBox : WarehouseBlock

enum class MapType { NORMAL, WIDE }


class Warehouse(private val inputLines: List<String>, private val mapType: MapType = NORMAL) {

    private val pointLines by lazy { inputLines.filter { it.startsWith('#') } }
    private val movementLines by lazy { inputLines.filterNot { it.startsWith('#') }.filterNot { it.isEmpty() } }

    private val initialRobotPosition: Point by lazy {
        var result: Point? = null
        pointLines.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                if (char == '@') {
                    result = Point(if (mapType == NORMAL) col else col * 2, row)
                }
            }
        }
        result ?: throw IllegalStateException("No starting point found")
    }

    private val map: MapOfThings<WarehouseBlock> by lazy {
        val points = when (mapType) {
            NORMAL -> pointsForNormalMapType()
            WIDE -> pointsForWideMapType()
        }

        val width = when (mapType) {
            NORMAL -> pointLines.first().length
            WIDE -> pointLines.first().length * 2
        }
        MapOfThings(points, width, pointLines.size)
    }

    private fun pointsForNormalMapType(): MutableMap<Point, WarehouseBlock> {
        val points = mutableMapOf<Point, WarehouseBlock>()
        pointLines.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                if (char == '#') {
                    points[Point(col, row)] = Wall
                } else if (char == 'O') {
                    points[Point(col, row)] = Box
                }
            }
        }
        return points
    }

    private fun pointsForWideMapType(): MutableMap<Point, WarehouseBlock> {
        val points = mutableMapOf<Point, WarehouseBlock>()
        pointLines.forEachIndexed { row, line ->
            line.forEachIndexed { col, char ->
                if (char == '#') {
                    points[Point(2 * col, row)] = Wall
                    points[Point(2 * col + 1, row)] = Wall
                } else if (char == 'O') {
                    points[Point(2 * col, row)] = LeftBox
                    points[Point(2 * col + 1, row)] = RightBox
                }
            }
        }
        return points
    }

    private val movementDirectives: List<Direction> by lazy {
        movementLines.joinToString("").map { char ->
            when (char) {
                '<' -> Direction.Left
                '>' -> Direction.Right
                '^' -> Direction.Up
                'v' -> Direction.Down
                else -> {
                    throw IllegalArgumentException("Unexpected movement: $char")
                }
            }
        }
    }

    fun walkAroundAndReturnSumOfGpsCoordinate(): Long {
        val updatedMap = walkAroundAndUpdateMap()

        return updatedMap.sumOfGpsCoordinates()
    }

    private fun walkAroundAndUpdateMap(): MapOfThings<WarehouseBlock> {
        var updatedMap = map
        var robotPosition = initialRobotPosition
        movementDirectives.forEach { direction ->
            val nextPosition = robotPosition.translate(1, direction)
            robotPosition = when (updatedMap.thingAt(nextPosition)) {
                is Box -> {
                    var lookAhead: Point = nextPosition
                    val boxesToMove = mutableSetOf<Point>()
                    do {
                        boxesToMove.add(lookAhead)
                        lookAhead = lookAhead.translate(1, direction)
                    } while (isNeitherWallNorEmpty(updatedMap, lookAhead))
                    if (updatedMap.thingAt(lookAhead) == null) {
                        // found empty space, move all boxes
                        updatedMap = newMapWithMovedBoxes(boxesToMove, direction, updatedMap)
                        nextPosition
                    } else {
                        // hit wall or left map, don't move
                        robotPosition
                    }
                }

                is Wall -> robotPosition
                null -> nextPosition
                else -> TODO()
            }
        }
        return updatedMap
    }

    private fun isNeitherWallNorEmpty(currentMap: MapOfThings<WarehouseBlock>, lookAhead: Point) =
        currentMap.thingAt(lookAhead) != null && (currentMap.thingAt(lookAhead) != Wall) && lookAhead within currentMap

    private fun newMapWithMovedBoxes(
        boxesToMove: Set<Point>,
        direction: Direction,
        oldMap: MapOfThings<WarehouseBlock>
    ): MapOfThings<WarehouseBlock> {
        val movedBoxes = boxesToMove.map { it.translate(1, direction) }
        return oldMap.updatedMap { pointMap ->
            boxesToMove.forEach(pointMap::remove)
            movedBoxes.forEach { pointMap[it] = Box }
        }
    }

    private fun MapOfThings<WarehouseBlock>.sumOfGpsCoordinates() = points()
        .sumOf { point ->
            if (thingAt(point) == Box) {
                100 * point.row + point.col
            } else {
                0L
            }
        }

    fun mapAsString(): String {
        val result = StringBuilder()
        (0..<map.height).forEach { row ->
            (0..<map.width).forEach { col ->
                val point = Point(col, row)
                if (initialRobotPosition == point) {
                    result.append('@')
                } else {
                    val char = when (map.thingAt(point)) {
                        Box -> 'O'
                        LeftBox -> '['
                        RightBox -> ']'
                        Wall -> '#'
                        null -> '.'
                    }
                    result.append(char)
                }
            }
            result.append("\n")
        }
        return result.toString().trim()
    }
}