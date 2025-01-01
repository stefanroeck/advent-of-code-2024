package day15

import day15.MapType.NORMAL
import day15.MapType.WIDE
import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Direction.Left
import util.MapOfThings.Direction.Right
import util.MapOfThings.Point

sealed interface WarehouseBlock
sealed interface WarehouseBox

data object Wall : WarehouseBlock

data object Box : WarehouseBlock, WarehouseBox
data object LeftBox : WarehouseBlock, WarehouseBox
data object RightBox : WarehouseBlock, WarehouseBox

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
                '<' -> Left
                '>' -> Right
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
            val nextBlock = updatedMap.thingAt(nextPosition)
            robotPosition = when (nextBlock) {
                is Box, LeftBox, RightBox -> {
                    val adjacentBoxesToBeMoved = updatedMap.adjacentBoxesToBeMoved(nextPosition, direction)
                    if (updatedMap.canMoveBoxes(adjacentBoxesToBeMoved, direction)) {
                        // found empty space, move all boxes
                        updatedMap = updatedMap.newMapWithMovedBoxes(adjacentBoxesToBeMoved, direction)
                        nextPosition
                    } else {
                        // hit wall or left map, don't move
                        robotPosition
                    }
                }

                is Wall -> robotPosition
                null -> nextPosition
            }
        }
        return updatedMap
    }

    private fun MapOfThings<WarehouseBlock>.newMapWithMovedBoxes(
        boxesToMove: Set<Point>,
        direction: Direction,
    ): MapOfThings<WarehouseBlock> {
        val movedBoxes = boxesToMove
            .map { it to thingAt(it)!! }
            .map { it.first.translate(1, direction) to it.second }
        return updatedMap { pointMap ->
            boxesToMove.forEach(pointMap::remove)
            movedBoxes.forEach { pointMap[it.first] = it.second }
        }
    }

    private fun MapOfThings<WarehouseBlock>.sumOfGpsCoordinates() = points()
        .sumOf { point ->
            if (thingAt(point) == Box || thingAt(point) == LeftBox) {
                100 * point.row + point.col
            } else {
                0L
            }
        }

    private fun MapOfThings<WarehouseBlock>.canMoveBoxes(boxesToBeMoved: Set<Point>, direction: Direction) =
        boxesToBeMoved
            .map { it.translate(1, direction) }
            .all { it within this && (thingAt(it) == null || thingAt(it) is WarehouseBox) }

    private fun MapOfThings<WarehouseBlock>.adjacentBoxesToBeMoved(
        boxPosition: Point,
        direction: Direction
    ): Set<Point> {
        val allBoxesToBeMoved = mutableSetOf<Point>()

        val nextPositionsToBeChecked = mutableListOf(boxPosition)
        do {
            nextPositionsToBeChecked.removeFirstOrNull()?.let { nextPosition ->
                val box = thingAt(nextPosition)
                if (box is WarehouseBox && nextPosition within this) {
                    val allBoxPositions = if (direction.isVertical()) when (box) {
                        // we only need to consider box widths for vertical movement
                        Box -> listOf(nextPosition)
                        LeftBox -> listOf(nextPosition, nextPosition.translate(1, Right))
                        RightBox -> listOf(nextPosition, nextPosition.translate(1, Left))
                        else -> throw IllegalStateException("$box is not a valid box")
                    } else {
                        // for horizontal movements, just treat two-width boxes like two separate ones
                        listOf(nextPosition)
                    }
                    allBoxesToBeMoved.addAll(allBoxPositions)

                    nextPositionsToBeChecked.addAll(allBoxPositions.map { it.translate(1, direction) })
                }
            }
        } while (nextPositionsToBeChecked.isNotEmpty())
        return allBoxesToBeMoved
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