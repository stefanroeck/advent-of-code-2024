package day10

import util.MapOfThings
import util.MapOfThings.Point

data class Height(val height: Int)

val unknownHeight = Height(-99)

class HikingTrails(lines: List<String>) {

    private val trailMap = MapOfThings.parse(lines) { if (it != '.') Height(it.digitToInt()) else unknownHeight }

    fun parseMap(): MapOfThings<Height> {
        return trailMap
    }

    fun trailHeads() = trailMap.pointsFor(Height(0))

    fun score() = trailHeads().sumOf {
        distinctTrails(it).map { path -> path.last() }.distinct().size
    }

    private fun distinctTrails(trailHead: Point): Set<List<Point>> {
        var currentHeight = 0
        val distinctPathes = mutableListOf(listOf(trailHead))
        do {
            currentHeight++
            val pathesIterator = distinctPathes.listIterator()
            pathesIterator.forEach { path ->
                val adjacentPoints =
                    trailMap.adjacentPoints(path.last()).filter { trailMap.thingAt(it)!!.height == currentHeight }
                pathesIterator.remove()
                adjacentPoints.forEach {
                    pathesIterator.add(path + it)
                }
            }
        } while (distinctPathes.isNotEmpty() && currentHeight < 9)

        return distinctPathes.toSet()
    }

    fun distinctHikingTrails(): Int {
        return trailHeads().sumOf {
            distinctTrails(it).size
        }
    }
}