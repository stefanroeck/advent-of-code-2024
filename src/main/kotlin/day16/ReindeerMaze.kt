package day16

import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Point

private enum class Maze {
    Wall, Start, End, Empty
}

private data class PointWithDirection(val point: Point, val direction: Direction)

private interface PathOptimizationStrategy {
    fun abortTraversal(pointWithDirection: PointWithDirection, currentCosts: Long): Boolean
}

private class CheckVisitedPointsForLowerCosts : PathOptimizationStrategy {
    private val visitedPointsWithLowestCosts = mutableMapOf<Point, Long>()
    override fun abortTraversal(pointWithDirection: PointWithDirection, currentCosts: Long): Boolean {
        val result = visitedPointsWithLowestCosts[pointWithDirection.point]?.let { previousCosts ->
            previousCosts < currentCosts
        } ?: false
        if (result) {
            return true
        }
        visitedPointsWithLowestCosts[pointWithDirection.point] = currentCosts
        return false
    }
}

private class CheckVisitedPointsWithDirectionForLowerCosts : PathOptimizationStrategy {
    private val visitedPointsWithLowestCosts = mutableMapOf<PointWithDirection, Long>()
    override fun abortTraversal(pointWithDirection: PointWithDirection, currentCosts: Long): Boolean {
        val result = visitedPointsWithLowestCosts[pointWithDirection]?.let { previousCosts ->
            // we've been here for lower costs already. if the costs are the same,
            // we continue as we need all paths
            previousCosts < currentCosts
        } ?: false
        if (result) {
            return true
        }
        visitedPointsWithLowestCosts[pointWithDirection] = currentCosts
        return false
    }
}

private class CombinedOptimizationStrategy(private val delegates: List<PathOptimizationStrategy>) :
    PathOptimizationStrategy {
    override fun abortTraversal(pointWithDirection: PointWithDirection, currentCosts: Long): Boolean {
        delegates.forEach { delegate ->
            if (delegate.abortTraversal(pointWithDirection, currentCosts)) {
                return true
            }
        }
        return false
    }

}

private class CheckForKnownTotalCosts(private val maximumCosts: Long) : PathOptimizationStrategy {
    override fun abortTraversal(pointWithDirection: PointWithDirection, currentCosts: Long): Boolean {
        return currentCosts > maximumCosts
    }
}


private data class PathThroughMaze(val path: List<Point>, val costs: Long)

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
        val solvedPaths = mutableListOf<PathThroughMaze>()

        move(startPosition, Direction.Right, CheckVisitedPointsForLowerCosts(), 0, solvedPaths, listOf(startPosition))

        return solvedPaths.minOf { it.costs }
    }

    fun seatsOnShortestPaths(): Long {
        // determine lowest costs (part 1)
        val shortestPathCosts = shortestPathCost()

        // traverse again and visit all paths that result in lowest determined cost
        // also make sure to visit all the same points in all directions as this could result in
        // additional paths with the same costs
        val pathOptimizationStrategy = CombinedOptimizationStrategy(
            listOf(
                CheckVisitedPointsWithDirectionForLowerCosts(),
                CheckForKnownTotalCosts(shortestPathCosts),
            )
        )
        val solvedPaths = mutableListOf<PathThroughMaze>()

        move(startPosition, Direction.Right, pathOptimizationStrategy, 0, solvedPaths, listOf(startPosition))

        val distinctPoints = solvedPaths
            .flatMap { it.path }
            .distinct()
        return distinctPoints.count().toLong()
    }

    private fun move(
        position: Point,
        direction: Direction,
        pathOptimizationStrategy: PathOptimizationStrategy,
        costs: Long,
        solvedPathsCosts: MutableList<PathThroughMaze>,
        path: List<Point>
    ) {
        if (position == endPosition) {
            println("Found path for costs $costs")
            solvedPathsCosts.add(PathThroughMaze(path, costs))
            return
        }

        if (solvedPathsCosts.any { costs > it.costs }) {
            return // already found a cheaper way
        }

        if (pathOptimizationStrategy.abortTraversal(PointWithDirection(position, direction), costs)) {
            return
        }

        val possibleDirections = Direction.xyDirections()
            .filter { direction != it.inverse() } // no u-turns
            .map { it to position.translate(1, it) }
            .filter { map.thingAt(it.second) != Maze.Wall } // don't run into walls

        if (possibleDirections.isEmpty()) {
            return // dead end
        }

        // traverse same direction first as way cheaper than turning
        val lowCostDirection = possibleDirections.firstOrNull { it.first == direction }?.let {
            move(it.second, direction, pathOptimizationStrategy, costs + 1, solvedPathsCosts, path + it.second)
            it
        }

        possibleDirections
            .filter { it != lowCostDirection }
            .forEach {
                move(
                    it.second,
                    it.first,
                    pathOptimizationStrategy,
                    costs + 1000 + 1,
                    solvedPathsCosts,
                    path + it.second
                )
            }
    }
}