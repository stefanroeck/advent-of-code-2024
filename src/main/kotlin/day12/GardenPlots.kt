package day12

import util.MapOfThings
import util.MapOfThings.Direction
import util.MapOfThings.Direction.Down
import util.MapOfThings.Direction.Left
import util.MapOfThings.Direction.Right
import util.MapOfThings.Direction.Up
import util.MapOfThings.Point

data class Plant(val letter: Char)

data class Region(val plant: Plant, val points: MutableSet<Point>, val map: MapOfThings<Plant>) {
    val sides: Int
        get() {
            // numbers define a side, if they are adjacent and the neighbor point is foreign territory
            val horizontalSides = points.groupBy { it.row }.flatMap { (row, rowPoints) ->
                horizontalSides(rowPoints, row, Up) + horizontalSides(rowPoints, row, Down)
            }

            val verticalSides = points.groupBy { it.col }.flatMap { (col, colPoints) ->
                verticalSides(colPoints, col, Left) + verticalSides(colPoints, col, Right)
            }

            println("Region $plant horizontalSides: $horizontalSides verticalSides: $verticalSides")
            return horizontalSides.size + verticalSides.size
        }

    private fun horizontalSides(rowPoints: List<Point>, row: Int, direction: Direction) = rowPoints
        .map { it.col }
        .filter { col -> map.thingAt(Point(col, row).translate(1, direction)) != plant }
        .consecutiveNumbers()

    private fun verticalSides(colPoints: List<Point>, col: Int, direction: Direction) = colPoints
        .map { it.row }
        .filter { row -> map.thingAt(Point(col, row).translate(1, direction)) != plant }
        .consecutiveNumbers()

    val priceWithPerimeter: Int
        get() = area * perimeter
    val priceWithSides: Int
        get() = area * sides
    val area: Int
        get() = points.size
    val perimeter: Int
        get() {
            return points.sumOf { point ->
                val adjacentPoints = map.adjacentPoints(point)
                adjacentPoints.map { adjacentPoint ->
                    if (map.thingAt(adjacentPoint) != plant) 1 else 0
                }.sum() + 4 - adjacentPoints.size
            }
        }
}

class GardenPlots(lines: List<String>) {

    private val map = MapOfThings.parse(lines) { Plant(it) }

    fun findRegions(): List<Region> {
        val regions = loopOverAllPointsAndDeriveRegions()

        // merge regions that were created as separate but are interconnected
        var mergedRegions: List<Region> = regions
        var previousMergedRegions: List<Region>
        do {
            previousMergedRegions = mergedRegions
            mergedRegions = mergeAdjacentRegions(previousMergedRegions)
        } while (mergedRegions.size != previousMergedRegions.size)

        return mergedRegions
    }

    private fun loopOverAllPointsAndDeriveRegions(): List<Region> {
        val regions = mutableListOf<Region>()

        map.points().forEach { point ->
            val adjacentPoints = map.adjacentPoints(point)
            val plant = map.thingAt(point)!!
            val existingRegion =
                regions.firstOrNull { region ->
                    adjacentPoints.any { adjacentPoint ->
                        region.points.contains(
                            adjacentPoint
                        ) && map.thingAt(adjacentPoint) == plant
                    }
                }
            val region = existingRegion ?: Region(plant, mutableSetOf(), map).also { regions.add(it) }
            region.points.add(point)
        }
        return regions.toList()
    }

    private fun mergeAdjacentRegions(regions: List<Region>): List<Region> {
        val regionsToRemove = mutableListOf<Region>()
        regions.forEach() { region ->
            val adjacentPointsWithSamePlantButOtherRegion = region.points.flatMap { point ->
                map.adjacentPoints(point).filter { adjacentPoint ->
                    map.thingAt(adjacentPoint) == region.plant && !region.points.contains(adjacentPoint)
                }
            }

            // don't merge in both directions
            if (!regionsToRemove.contains(region)) {
                adjacentPointsWithSamePlantButOtherRegion.map { adjacentPoint ->
                    regions.first { it.points.contains(adjacentPoint) }
                }.distinct().forEach { adjacentRegion ->
                    check(adjacentRegion != region) { "Regions to merge must be different" }
                    check(adjacentRegion.plant == region.plant) { "Regions to merge must have same letter" }
                    println("Merging points from ${adjacentRegion.plant} with neighbour region")
                    region.points.addAll(adjacentRegion.points)
                    regionsToRemove.add(adjacentRegion)
                }
            }
        }

        return regions.filterNot { regionsToRemove.contains(it) }
    }

    fun calculatePriceWithPerimeter(): Int {
        return findRegions().sumOf { region -> region.priceWithPerimeter }
    }

    fun calculatePriceWithSides(): Int {
        return findRegions().sumOf { region -> region.priceWithSides }
    }
}

fun List<Int>.consecutiveNumbers(): List<List<Int>> {
    return sorted().fold(mutableListOf()) { result, int ->
        if (result.isEmpty()) {
            result.add(listOf(int))
        } else if (result.last().last() == int - 1) {
            result.set(result.lastIndex, result.last() + int)
        } else {
            result.add(listOf(int))
        }
        result
    }
}