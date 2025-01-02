package day16

import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Point

private enum class Maze {
    Wall, Start, End, Empty
}

class ReindeerMaze(private val lines: List<String>) {

    private val map: MapOfThings<Maze> by lazy {
        MapOfThings.parse(lines) { c ->
            when (c) {
                '#' -> Maze.Wall
                'S' -> Maze.Start
                'E' -> Maze.End
                '.' -> Maze.Empty
                else -> {
                    throw IllegalArgumentException("Unknown char: $c")
                }
            }
        }
    }

    private val startPosition by lazy { map.pointsFor(Maze.Start).single() }

    private val endPosition by lazy {
        map.pointsFor(Maze.End).single()
    }

    fun shortestPathCost(): Long {
        val solvedPathsCosts = mutableSetOf<Long>()
        val visitedJunctionsWithLowestCosts = mutableMapOf<Point, Long>()

        move(startPosition, Direction.Right, visitedJunctionsWithLowestCosts, 0, solvedPathsCosts)

        return solvedPathsCosts.minOf { it }
    }

    private fun move(
        position: Point,
        direction: Direction,
        visitedJunctionsWithLowestCosts: MutableMap<Point, Long>,
        costs: Long,
        solvedPathsCosts: MutableSet<Long>
    ) {
        if (position == endPosition) {
            println("Found path for costs $costs")
            solvedPathsCosts.add(costs)
            return
        }

        visitedJunctionsWithLowestCosts[position]?.let { visitedCosts ->
            if (visitedCosts < costs) {
                return // already been here for lower costs
            }
        }

        if (solvedPathsCosts.any { costs >= it }) {
            return // already found a cheaper way
        }

        val possibleDirections = Direction.xyDirections()
            .filter { direction != it.inverse() } // no u-turns
            .map { it to position.translate(1, it) }
            .filter { map.thingAt(it.second) != Maze.Wall } // don't run into walls

        if (possibleDirections.isEmpty()) {
            return // dead end
        }

        if (possibleDirections.size > 1) {
            visitedJunctionsWithLowestCosts[position] = costs
        }

        // traverse same direction first as way cheaper than turning
        val lowCostDirection = possibleDirections.firstOrNull { it.first == direction }?.let {
            move(it.second, direction, visitedJunctionsWithLowestCosts, costs + 1, solvedPathsCosts)
            it
        }

        possibleDirections
            .filter { it != lowCostDirection }
            .forEach {
                move(it.second, it.first, visitedJunctionsWithLowestCosts, costs + 1000 + 1, solvedPathsCosts)
            }

    }
}